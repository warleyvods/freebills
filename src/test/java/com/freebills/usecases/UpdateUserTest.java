package com.freebills.usecases;

import com.freebills.domain.User;
import com.freebills.exceptions.PermissionDeniedException;
import com.freebills.gateways.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private FindUser findUser;

    @InjectMocks
    private UpdateUser updateUser;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin("user");
        user.setPassword("password");
        user.setLastLogin("user");
    }

    @Test
    void updateUserShouldNotThrowExceptionWhenUserIsNotAdmin() {
        when(findUser.byId(anyLong())).thenReturn(user);
        assertDoesNotThrow(() -> updateUser.execute(user));
    }

    @Test
    void updateUserPasswordShouldThrowExceptionWhenPasswordIsIncorrect() {
        User userFinded = new User();
        userFinded.setPassword("wrongPassword");
        assertThrows(PermissionDeniedException.class, () -> updateUser.updateUserPassword(user, userFinded));
    }

    @Test
    void updateUserPasswordShouldThrowExceptionWhenUserIsAdmin() {
        user.setLogin("admin");
        assertThrows(PermissionDeniedException.class, () -> updateUser.updateUserPassword(user, user));
    }

    @Test
    void updatePasswordShouldThrowExceptionWhenUserIsAdmin() {
        user.setLogin("admin");
        assertThrows(PermissionDeniedException.class, () -> updateUser.updatePassword(user));
    }
}