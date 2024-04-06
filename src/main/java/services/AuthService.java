package services;
import org.jasypt.util.text.BasicTextEncryptor;

public class AuthService {
  private final BasicTextEncryptor textEncryptor;

  public AuthService(BasicTextEncryptor textEncryptor) {
    this.textEncryptor = textEncryptor;
    textEncryptor.setPasswordCharArray("nhf8HEP-nhn3duj9uva".toCharArray());
  }

  public String encryptText(String text){
    return textEncryptor.encrypt(text);
  }

  public String decryptText(String encryptedText) {
    return textEncryptor.decrypt(encryptedText);
  }
}