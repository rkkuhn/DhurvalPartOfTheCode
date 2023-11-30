public class CaesarCipher {
    private static final int SHIFT = 3; // You can change this shift value as needed

    // Encrypt a message using Caesar cipher
    public static String encrypt(String message) {
        StringBuilder encryptedMessage = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (c + SHIFT);
                if ((Character.isLowerCase(c) && shiftedChar > 'z') ||
                    (Character.isUpperCase(c) && shiftedChar > 'Z')) {
                    shiftedChar -= 26; // Wrap around the alphabet
                }
                encryptedMessage.append(shiftedChar);
            } else {
                encryptedMessage.append(c); // Keep non-letter characters unchanged
            }
        }

        return encryptedMessage.toString();
    }

    // Decrypt a message using Caesar cipher
    public static String decrypt(String encryptedMessage) {
        StringBuilder decryptedMessage = new StringBuilder();

        for (char c : encryptedMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (c - SHIFT);
                if ((Character.isLowerCase(c) && shiftedChar < 'a') ||
                    (Character.isUpperCase(c) && shiftedChar < 'A')) {
                    shiftedChar += 26; // Wrap around the alphabet
                }
                decryptedMessage.append(shiftedChar);
            } else {
                decryptedMessage.append(c); // Keep non-letter characters unchanged
            }
        }

        return decryptedMessage.toString();
    }
}
