package encryptdecrypt;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        String mode = "enc";
        String data = "";
        int key = 0;
        String fileIn = "";
        String fileOut = "";
        String algorithm = "-shift";
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-mode")) {
                mode = args[i + 1];
            }
            if (args[i].equals("-data")) {
                data = args[i + 1];
            }
            if (args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-in")) {
                fileIn = args[i + 1];
            }
            if (args[i].equals("-out")) {
                fileOut = args[i + 1];
            }
            if (args[i].equals("-alg")) {
                algorithm = args[i + 1];
            }
        }
        if (algorithm.equals("unicode")) {
            String input = "";
            if (!fileIn.equals("")) {
                try {
                    input = readFileAsString(fileIn);
                } catch (IOException e) {
                    System.out.println("Cannot read file: " + e.getMessage());
                }
            } else {
                input = data;
            }
            if (fileOut.equals("")) {
                System.out.println(encryptOrDecrypt(mode, input, key));
            } else {
                writeToFile(fileOut, encryptOrDecrypt(mode, input, key));
            }
        } else if (algorithm.equals("shift")) {
            String input = "";
            if (!fileIn.equals("")) {
                try {
                    input = readFileAsString(fileIn);
                } catch (IOException e) {
                    System.out.println("Cannot read file: " + e.getMessage());
                }
            } else {
                input = data;
            }
            if (fileOut.equals("")) {
                System.out.println(encryptShift(input, key, mode));
            } else {
                writeToFile(fileOut, encryptShift(input, key, mode));
            }
        }
    }

    public static void writeToFile(String path, String data) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(data);
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static String encryptOrDecrypt(String action, String a, int key) {
        StringBuilder sb = new StringBuilder();
        if (action.equals("dec")) key = -key;
        for (char b : a.toCharArray()) {
            sb.append((char) (b + key));
        }
        return sb.toString();
    }

    static String encryptShift(String message, int key, String mode) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String[] result = new String[message.length()];
        if (mode.equals("dec")) {
            for (int i = 0; i < message.length(); i++) {
                if (Character.isAlphabetic(message.charAt(i))) {
                    int index = alphabet.indexOf(message.toLowerCase().charAt(i));
                    char ch = message.charAt(i);
                    if (index - key >= 0) {
                        if (Character.isUpperCase(ch)) {
                            result[i] = String.valueOf(alphabet.toUpperCase().charAt(Math.abs(index - key)));
                        } else {
                            result[i] = String.valueOf(alphabet.toLowerCase().charAt(Math.abs(index - key)));
                        }
                    } else {
                        if (Character.isUpperCase(ch)) {
                            result[i] = String.valueOf(alphabet.toUpperCase().charAt(26 - Math.abs(index - key)));
                        } else {
                            result[i] = String.valueOf(alphabet.toLowerCase().charAt(26 - Math.abs(index - key)));
                        }

                    }
                } else if (String.valueOf(message.charAt(i)).equals(" ")) {
                    result[i] = " ";
                } else {
                    result[i] = String.valueOf(message.charAt(i));
                }
            }
            String r = String.join("", result);
            return r;
        } else if(mode.equals("enc")) {
            for (int i = 0; i < message.length(); i++) {
                if (Character.isAlphabetic(message.charAt(i))) {
                    int index = alphabet.indexOf(message.toLowerCase().charAt(i));
                    char ch = message.charAt(i);
                    if (index + key <= 26) {
                        if (Character.isUpperCase(ch)) {
                            result[i] = String.valueOf(alphabet.toUpperCase().charAt(index + key));
                        } else {
                            result[i] = String.valueOf(alphabet.toLowerCase().charAt(index + key));
                        }
                    } else if (index + key > 26) {
                        if (Character.isUpperCase(ch)) {
                            result[i] = String.valueOf(alphabet.toUpperCase().charAt((index + key) - 26));
                        } else {
                            result[i] = String.valueOf(alphabet.toLowerCase().charAt((index + key) - 26));
                        }

                    }
                } else if (String.valueOf(message.charAt(i)).equals(" ")) {
                    result[i] = " ";
                } else {
                    result[i] = String.valueOf(message.charAt(i));
                }
            }
            String r = String.join("", result);
            return r;
        }
        return "";
    }

}