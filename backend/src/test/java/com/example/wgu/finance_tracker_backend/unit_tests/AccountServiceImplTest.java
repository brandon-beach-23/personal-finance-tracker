package com.example.wgu.finance_tracker_backend.unit_tests;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.models.Account;
import com.example.wgu.finance_tracker_backend.models.AccountType;
import com.example.wgu.finance_tracker_backend.models.User;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
import com.example.wgu.finance_tracker_backend.repositories.UserRepository;
import com.example.wgu.finance_tracker_backend.services.implementations.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_Success() {
        // Arrange
        String username = "testuser";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUserName(username);

        AccountRequest request = new AccountRequest();
        request.setAccountName("My Checking");
        request.setBalance(BigDecimal.valueOf(500));
        request.setAccountType("CHECKING");

        Account expectedAccount = new Account();
        expectedAccount.setId(1L);
        expectedAccount.setAccountName("My Checking");
        expectedAccount.setBalance(BigDecimal.valueOf(500));
        expectedAccount.setAccountType(AccountType.CHECKING);
        expectedAccount.setUser(mockUser);

        Mockito.when(userRepository.findByUserName(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(accountRepository.existsByAccountNameAndUser("My Checking", mockUser)).thenReturn(false);
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(expectedAccount);

        // Act
        AccountResponse response = accountService.createAccount(request, username);

        // Assert
        assertEquals("My Checking", response.getAccountName());
        assertEquals(BigDecimal.valueOf(500), response.getBalance());
        assertEquals("CHECKING", response.getAccountType());
        assertEquals(1L, response.getUserId());

        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }
}

