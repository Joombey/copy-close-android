package dev.farukh.copyclose.core.data.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import db.CopyCloseDB
import dev.farukh.copyclose.core.data.dto.UserDTO
import dev.farukh.copyclose.core.utils.extensions.long
import dev.farukh.network.core.AddressCore
import dev.farukh.network.core.RoleCore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserLocalDataSource(private val db: CopyCloseDB) {
    val activeUser = db.userQueries.activeUser()
        .asFlow()
        .mapToOneOrNull(Dispatchers.IO)
        .map { user ->
            user?.id to (user?.let { getRole(it.roleID).canBan == 1L } ?: false)
        }
        .distinctUntilChanged()

    suspend fun createOrUpdateUser(
        role: RoleCore,
        user: UserDTO,
        address: AddressCore,
    ) {
        withContext(Dispatchers.IO) {
            if (db.addressQueries.addressExists(address.id!!).executeAsOne()) {
                db.addressQueries.updateAddress(
                    id = address.id!!,
                    addressName = address.addressName,
                    lat = address.lat,
                    lon = address.lon
                )
            } else {
                db.addressQueries.createAddress(
                    id = address.id!!,
                    addressName = address.addressName,
                    lat = address.lat,
                    lon = address.lon
                )
            }
            if (db.roleQueries.roleExists(id = role.id.toLong()).executeAsOne()) {
                db.roleQueries.updateRole(
                    id = role.id.toLong(),
                    canBuy = role.canBuy.long,
                    canBan = role.canBan.long,
                    canSell = role.canSell.long
                )
            } else {
                db.roleQueries.createRole(
                    id = role.id.toLong(),
                    canBuy = role.canBuy.long,
                    canBan = role.canBan.long,
                    canSell = role.canSell.long
                )
            }
            if (db.userQueries.userExists(user.id).executeAsOne()) {
                db.userQueries.updateUser(
                    id = user.id,
                    name = user.name,
                    icon = user.icon,
                    roleID = role.id.toLong(),
                    addressID = address.id!!,
                    authToken = user.authToken,
                    iconID = user.iconUrl
                )
            } else {
                db.userQueries.createUser(
                    id = user.id,
                    name = user.name,
                    icon = user.icon,
                    roleID = role.id.toLong(),
                    addressID = address.id!!,
                    authToken = user.authToken,
                    iconID = user.iconUrl
                )
            }
        }
    }

    suspend fun checkImageValid(userID: String, imageID: String): Boolean =
        withContext(Dispatchers.IO) {
            db.userQueries.getIconByID(userID).executeAsOneOrNull() == imageID
        }

    suspend fun userExists(userID: String): Boolean = withContext(Dispatchers.IO) {
        db.userQueries.userExists(userID).executeAsOne()
    }

    suspend fun makeUserActive(userID: String) = withContext(Dispatchers.IO) {
        db.userQueries.transaction {
            db.userQueries.makeUserActive(userID)
            db.userQueries.makeOtherInActive(userID)
        }
    }

    suspend fun makeUserInActive(userID: String) = withContext(Dispatchers.IO) {
        db.userQueries.makeUserInActive(userID)
    }

    suspend fun getActiveUser() = withContext(Dispatchers.IO) {
        db.userQueries.activeUser().executeAsOneOrNull()
    }

    suspend fun getUserByID(userID: String) = withContext(Dispatchers.IO) {
        db.userQueries.getUserByID(userID).executeAsOneOrNull()
    }

    suspend fun getRole(roleID: Int) = withContext(Dispatchers.IO) {
        db.roleQueries.getRoleByID(roleID.toLong()).executeAsOne()
    }

    suspend fun getRole(roleID: Long) = withContext(Dispatchers.IO) {
        db.roleQueries.getRoleByID(roleID).executeAsOne()
    }

    suspend fun getAddressByID(addressID: String) = withContext(Dispatchers.IO) {
        db.addressQueries.getAddressByID(addressID).executeAsOne()
    }
}