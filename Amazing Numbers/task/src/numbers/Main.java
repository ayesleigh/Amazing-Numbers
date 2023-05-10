package numbers;

import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Map<String, Boolean> properties = new LinkedHashMap<>();
    static String instructions = """
                             Supported requests:
                             - enter a natural number to know its properties;
                             - enter two natural numbers to obtain the properties of the list:
                               * the first parameter represents a starting number;
                               * the second parameter shows how many consecutive numbers are to be printed;
                             - two natural numbers and properties to search for;
                             - a property preceded by minus must not be present in numbers;
                             - separate the parameters with one space;
                             - enter 0 to exit.
                                 """;
    static String firstParameterError = "The first parameter should be a " +
            "natural number or zero.\n";
    static String secondParameterError = "The second parameter should be a " +
            "natural number.\n";
    static String availableProperties = String.format("Available properties: " +
            "%s\n", Arrays.asList(Property.values()));


    public static void main(String[] args) {
        welcome();

        do {
            System.out.print("Enter a request: ");
            String input = scanner.nextLine().toUpperCase();
            System.out.println();

            if (input.isEmpty()) {
                printInstructions();
                continue;
            }

            String[] array = input.split(" ");
            long n;
            try {
                n = Long.parseLong(array[0]);
                if (n < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException numberFormatException) {
                System.out.println(firstParameterError);
                continue;
            }

            if (n == 0) {
                break;
            }

            getProperties(n);

            if (array.length == 1) {
                printAllProperties(n);
            } else {
                long count;
                try {
                    count = Long.parseLong(array[1]);
                    if (count <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException numberFormatException) {
                    System.out.println(secondParameterError);
                    continue;
                }

                if (array.length == 2) {
                    printListOfNumbersAndTheirProperties(n, count);
                } else {
                    String[] inputProperties = Arrays.copyOfRange(array, 2,
                            array.length);
                    filterAndSearchForSpecifiedProperties(n,
                            count, inputProperties);
                }
            }
        } while (true);

        System.out.println("Goodbye!");
    }

    static void welcome() {
        System.out.println("\nWelcome to Amazing Numbers!\n");
        printInstructions();
    }

    static void printInstructions() {
        System.out.println(instructions);
    }

    static void filterAndSearchForSpecifiedProperties(long n, long count,
                                                      String[] inputProperties) {
        if (checkUnexpectedProperties(List.of(inputProperties))) {
            return;
        }

        List<String> positiveProperties = new ArrayList<>();
        List<String> negativeProperties = new ArrayList<>();
        for (String property : inputProperties) {
            if (property.startsWith("-")) {
                negativeProperties.add(property);
            } else {
                positiveProperties.add(property);
            }
        }

        if (checkPositiveExclusiveProperties(positiveProperties) ||
                checkNegativeExclusiveProperties(negativeProperties) ||
                checkSameExclusiveProperties(positiveProperties,
                        negativeProperties)) {
            return;
        }

        printFilteredListOfNumbersAndTheirProperties(n, count,
                positiveProperties, negativeProperties);
    }

    static boolean checkUnexpectedProperties(List<String> inputProperties) {
        List<String> wrongInputs = new ArrayList<>();

        for (String element : inputProperties) {
            String property = element;
            boolean hasDash = false;
            if (element.startsWith("-")) {
                hasDash = true;
                property = element.substring(1);
            }
            if (!properties.containsKey(property)) {
                wrongInputs.add(hasDash ? "-" + property : element);
            }
        }

        if (wrongInputs.size() > 0) {
            if (wrongInputs.size() == 1) {
                System.out.printf("The property %s is wrong.\n", wrongInputs);
            } else {
                System.out.printf("The properties %s are wrong.\n",
                        wrongInputs);
            }
            System.out.println(availableProperties);
            return true;
        } else {
            return false;
        }
    }

    static boolean checkPositiveExclusiveProperties(
            List<String> positiveProperties) {
        List<String> wrongInputs = new ArrayList<>();

        boolean oddAndEven =
                positiveProperties.contains(Property.EVEN.name())
                        && positiveProperties.contains(Property.ODD.name());
        boolean duckAndSpy =
                positiveProperties.contains(Property.DUCK.name())
                        && positiveProperties.contains(Property.SPY.name());
        boolean sunnyAndSquare =
                positiveProperties.contains(Property.SUNNY.name())
                        && positiveProperties.contains(Property.SQUARE.name());
        boolean happyAndSad =
                positiveProperties.contains(Property.HAPPY.name())
                        && positiveProperties.contains(Property.SAD.name());

        if (oddAndEven || duckAndSpy || sunnyAndSquare || happyAndSad) {
            if (oddAndEven) {
                wrongInputs.add(Property.EVEN.name());
                wrongInputs.add(Property.ODD.name());
            } else if (duckAndSpy) {
                wrongInputs.add(Property.DUCK.name());
                wrongInputs.add(Property.SPY.name());
            } else if (sunnyAndSquare){
                wrongInputs.add(Property.SUNNY.name());
                wrongInputs.add(Property.SQUARE.name());
            } else {
                wrongInputs.add(Property.HAPPY.name());
                wrongInputs.add(Property.SAD.name());
            }

            System.out.printf("""
               The request contains mutually exclusive properties: %s
               There are no numbers with these properties.

               """, wrongInputs);

            return true;
        } else {
            return false;
        }
    }

    static boolean checkNegativeExclusiveProperties(
            List<String> negativeProperties) {
        List<String> wrongInputs = new ArrayList<>();

        boolean oddAndEven =
                negativeProperties.contains("-" + Property.EVEN.name())
                        && negativeProperties.contains("-" + Property.ODD.name());
        boolean happyAndSad =
                negativeProperties.contains("-" + Property.HAPPY.name())
                        && negativeProperties.contains("-" + Property.SAD.name());

        if (oddAndEven || happyAndSad) {
            if (oddAndEven) {
                wrongInputs.add("-" + Property.EVEN.name());
                wrongInputs.add("-" + Property.ODD.name());
            } else {
                wrongInputs.add("-" + Property.HAPPY.name());
                wrongInputs.add("-" + Property.SAD.name());
            }

            System.out.printf("""
               The request contains mutually exclusive properties: %s
               There are no numbers with these properties.

               """, wrongInputs);

            return true;
        } else {
            return false;
        }
    }
    static boolean checkSameExclusiveProperties(List<String> positiveProperties,
                                             List<String> negativeProperties) {
        for (String property : negativeProperties) {
            if (positiveProperties.contains(property.substring(1))) {
                List<String> exclusiveProperties = new ArrayList<>();
                exclusiveProperties.add(property);
                exclusiveProperties.add(property.substring(1));
                System.out.printf("""
                       The request contains mutually exclusive properties: %s
                       There are no numbers with these properties.
                       
                       """, exclusiveProperties);
                return true;
            }
        }

        return false;
    }

    static void printFilteredListOfNumbersAndTheirProperties(long n, long count,
                                            List<String> positiveProperties,
                                            List<String> negativeProperties) {
        int countPositiveProperties;
        int countNegativeProperties;
        for (int i = 0; i < count; i++, n++) {
            while (true) {
                countPositiveProperties = 0;
                countNegativeProperties = 0;
                getProperties(n);
                for (String inputProperty : positiveProperties) {
                    if (properties.get(inputProperty)) {
                        ++countPositiveProperties;
                    }
                }
                for (String negativeProperty : negativeProperties) {
                    if (!properties.get(negativeProperty.substring(1))) {
                        ++countNegativeProperties;
                    }
                }
                if (countPositiveProperties == positiveProperties.size()
                        && countNegativeProperties == negativeProperties.size()) {
                    break;
                }
                n++;
            }
            printOnlyTruePropertiesInOneLine(n);
        }
        System.out.println();
    }

    static void printListOfNumbersAndTheirProperties(long n, long count) {
        for (int i = 0; i < count; i++, n++) {
            getProperties(n);
            printOnlyTruePropertiesInOneLine(n);
        }
        System.out.println();
    }

    static void getProperties(long n) {
        properties.put(Property.EVEN.name(), checkIfNumberIsEven(n));
        properties.put(Property.ODD.name(), checkIfNumberIsOdd(n));
        properties.put(Property.BUZZ.name(), checkIfNumberIsBuzz(n));
        properties.put(Property.DUCK.name(), checkIfNumberIsDuck(n));
        properties.put(Property.PALINDROMIC.name(), checkIfNumberIsPalindromic(n));
        properties.put(Property.GAPFUL.name(), checkIfNumberIsGapful(n));
        properties.put(Property.SPY.name(), checkIfNumberIsSpy(n));
        properties.put(Property.SQUARE.name(), checkIfNumberIsSquare(n));
        properties.put(Property.SUNNY.name(), checkIfNumberIsSunny(n));
        properties.put(Property.JUMPING.name(), checkIfNumberIsJumping(n));
        properties.put(Property.HAPPY.name(), checkIfNumberIsHappy(n));
        properties.put(Property.SAD.name(), checkIfNumberIsSad(n));
    }

    static void printAllProperties(long n) {
        System.out.printf("Properties of %,d\n", n);
        for (Map.Entry<String, Boolean> entry : properties.entrySet()) {
            System.out.printf("%12s: %b\n", entry.getKey().toLowerCase(),
                    entry.getValue());
        }
        System.out.println();
    }

    static void printOnlyTruePropertiesInOneLine(long n) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter fmt = new Formatter(stringBuilder);
        fmt.format("%,16d is ", n);

        for (Map.Entry<String, Boolean> entry : properties.entrySet()) {
            if (entry.getValue()) {
                stringBuilder.append(entry.getKey().toLowerCase()).append(", ");
            }
        }

        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        System.out.println(stringBuilder);
    }

    static boolean checkIfNumberIsEven(long n) {
        return n % 2 == 0;
    }

    static boolean checkIfNumberIsOdd(long n) {
        return n % 2 == 1;
    }

    static boolean checkIfNumberIsBuzz(long n) {
        return n % 7 == 0 || n % 10 == 7;
    }

    static boolean checkIfNumberIsDuck(long n) {
        String string = String.valueOf(n);
        ArrayList<String> list =
                new ArrayList<>(Arrays.asList(string.split("")));
        return list.contains("0");
    }

    static boolean checkIfNumberIsPalindromic(long n) {
        String string = String.valueOf(n);

        StringBuilder reversedString = new StringBuilder();
        reversedString.append(string);
        reversedString.reverse();

        return Objects.equals(string, String.valueOf(reversedString));
    }

    static boolean checkIfNumberIsGapful(long n) {
        String string = String.valueOf(n);
        if (string.length() < 3) {
            return false;
        }
        String firstDigit = String.valueOf(string.charAt(0));
        String lastDigit = String.valueOf(string.charAt(string.length() - 1));
        int gap = Integer.parseInt(firstDigit + lastDigit);

        return n % gap == 0;
    }

    static boolean checkIfNumberIsSpy(long n) {
        String string = String.valueOf(n);
        int sum = 0;
        int product = 1;
        for (int i = 0; i < string.length(); i++) {
            int digit = Character.getNumericValue(string.charAt(i));
            sum += digit;
            product *= digit;
        }

        return sum == product;
    }

    static boolean checkIfNumberIsSquare(long n) {
        double sqrt = Math.sqrt(n);
        return ((sqrt - Math.floor(sqrt)) == 0);
    }

    static boolean checkIfNumberIsSunny(long n) {
        return checkIfNumberIsSquare(n + 1);
    }

    static boolean checkIfNumberIsJumping(long n) {
        String string = String.valueOf(n);
        if (string.length() == 1) {
            return true;
        }

        int[] digits = new int[string.length()];
        for (int i = 0; i < string.length(); i++) {
            digits[i] = Character.getNumericValue(string.charAt(i));
        }

        for (int i = 0; i < digits.length - 1; i++) {
            if (Math.abs(digits[i] - digits[i + 1]) != 1) {
                return false;
            }
        }

        return true;
    }

    static boolean checkIfNumberIsHappy(long n) {
        String string = String.valueOf(n);
        if (string.length() == 1) {
            return n == 1 || n == 7;
        }

        n = 0;
        for (int i = 0; i < string.length(); i++) {
            int digit = Character.getNumericValue(string.charAt(i));
            n += Math.pow(digit, 2);
        }
        return checkIfNumberIsHappy(n);
    }

    static boolean checkIfNumberIsSad(long n) {
        return !checkIfNumberIsHappy(n);
    }
}
