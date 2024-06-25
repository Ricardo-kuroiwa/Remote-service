package client;

import compute.Task;
import java.io.Serializable;
import java.security.SecureRandom;
import java.io.UnsupportedEncodingException;


public class Services implements Task<String>, Serializable {

    private static final long serialVersionUID = 227L;
    private int lenght;
    private boolean incluedSpecialChars;
    private int service;
    private int comb_arg_1;
    private int comb_arg_2;
    private String texto;
    public Services(int lenght, boolean incluedSpecialChars,int service) {
        this.lenght = lenght;
        this.incluedSpecialChars=incluedSpecialChars;
        this.service=service;
    }
    public Services(String texto,int service) {
        this.texto = texto;
        this.service=service;
    }
    public Services(int comb_arg_1,int comb_arg_2,int service) {
        this.comb_arg_1 = comb_arg_1;
        this.comb_arg_2=comb_arg_2;
        this.service=service;
    }
    public String  execute()  {
        if(service==1){
            return computeGeneratePassword(lenght,incluedSpecialChars);
        }else if(service==2){
            return isPalindrome(texto);
        }else if(service==3){
            return validarSenha(texto);
        }else if(service==4){
            return calcularCombinacao(comb_arg_1,comb_arg_2);
        }else{
            return "Servico invalido";
        }
    }
    //Ele gera uma senha com ou sem caracter especial, de tamanho a ser passado pelo usuario
    public static String computeGeneratePassword(int lenght, boolean incluedSpecialChars ) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[]{}|;:,.<>?/";

        String allowedChars = upperCaseLetters + lowerCaseLetters + numbers;
        if (incluedSpecialChars) {
            allowedChars += specialChars;
        }
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(lenght);

        for (int i = 0; i < lenght; i++) {
            int index = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(index));
        }

        return password.toString();
        
    }
    //Ele verefica se dada string é um palindromo, ou seja, a string é igual a sua string reversa
    public static String isPalindrome(String input) {
        String reversed = new StringBuilder(input.toLowerCase()).reverse().toString();
        return input.equalsIgnoreCase(reversed) ? "Sim" : "Nao";
    }
    //Ele verefica se dada senha tem 8 caracters, pelo menos 1 numero e 1 letra maiuscula
    public static String validarSenha(String senha) {
        if (senha.length() < 8) {
            return "Senha muito curta";
        }
        boolean temLetraMaiuscula = false;
        boolean temNumero = false;
        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) {
                temLetraMaiuscula = true;
            } else if (Character.isDigit(c)) {
                temNumero = true;
            }
        }
        return (temLetraMaiuscula && temNumero )? "Senha valida" : "Senha invalida";
    }
    //Calcula o fatorial de dado numero interiro
    public static int fatorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    //Calcula a combinacao simples
    public static String calcularCombinacao(int arg1, int arg2){
        int comb = fatorial(arg1) / (fatorial(arg2) * fatorial(arg1 - arg2));
        return Integer.toString(comb);
    }

}
