package dev.farukh.copyclose.features.register.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.farukh.copyclose.R
import dev.farukh.copyclose.features.register.registerDI
import dev.farukh.copyclose.features.register.ui.RegisterViewModel
import dev.farukh.copyclose.utils.UiUtils
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberViewModel
import org.kodein.di.compose.withDI

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) = withDI(di = registerDI(localDI())) {
    val viewModel: RegisterViewModel by rememberViewModel()
    LaunchedEffect(key1 = viewModel.uiState.registered) {
        if (viewModel.uiState.registered) onRegisterSuccess()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UiUtils.arrangementDefault)
    ) {
        IconChooserView(
            icon = viewModel.uiState.userIcon,
            onChoose = viewModel::chooseIcon,
            modifier = Modifier.size(150.dp),
        )
        OutlinedTextField(
            value = viewModel.uiState.login,
            onValueChange = viewModel::setLogin,
            isError = viewModel.uiState.userExistsErr,
            label = {
                Text(stringResource(id = R.string.login))
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.uiState.password,
            onValueChange = viewModel::setPassword,
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(stringResource(id = R.string.password))
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.uiState.passwordConfirm,
            onValueChange = viewModel::setPasswordConfirm,
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(stringResource(id = R.string.password_confirm))
            },
            modifier = Modifier.fillMaxWidth()
        )

        QueryField(
            uiState = viewModel.uiState.queryUIState,
            onAddressClick = viewModel::chooseAddress,
            onQueryChange = viewModel::setQuery,
            onQueryClick = viewModel::query,
            label = {
                Text(stringResource(id = R.string.enter_address))
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}