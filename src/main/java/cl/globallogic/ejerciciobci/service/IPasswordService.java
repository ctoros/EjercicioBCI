package cl.globallogic.ejerciciobci.service;

public interface IPasswordService {

	String hash(String password);
    boolean verifyHash(String password, String hash);

}
