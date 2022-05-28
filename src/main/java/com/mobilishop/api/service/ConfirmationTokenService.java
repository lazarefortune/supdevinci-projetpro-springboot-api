package com.mobilishop.api.service;

import com.mobilishop.api.model.ConfirmationToken;
import com.mobilishop.api.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public void saveConfirmationToken(ConfirmationToken confirmationToken) {
    confirmationTokenRepository.save(confirmationToken);
  }

  public Optional<ConfirmationToken> getToken(String token) {
    return confirmationTokenRepository.findByToken(token);
  }

  public void setConfirmedAt(String token) {
    Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
    if (confirmationToken.isPresent()) {
      ConfirmationToken tokenEntity = confirmationToken.get();
      tokenEntity.setConfirmedAt(tokenEntity.getConfirmedAt());
      confirmationTokenRepository.save(tokenEntity);
    }
  }
}
