[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/6hlwF2x9)
# Project 2: Banking Application

## Project Overview

In this project, you will create `BankApp`, a banking tool that stores multiple accounts and allows users to deposit and withdraw funds, so long as they have the correct password.

### Objectives

- Continue practicing object-oriented design
- Introduce concepts of working with secrets and secure information
- Practice working with file I/O

## Building and Testing with Gradle

I have reworked the Gradle build system and test suites; hopefully you will find
the resulting build system easier to use and the tests easier to decipher. **You
should use the provided Gradle system to compile and test your project. If you
are unable to compile your code with Gradle, assume that the same will be true
for me.**

The provided `gradlew` wrapper (`gradlew.bat` on Windows) will first bootstrap
your system with the required version of Gradle, and then run all build steps.
You can use the following commands to interact with the build system. (On
Windows, substitute `.\gradlew.bat` for `./gradlew`.)

- `./gradlew assemble`

  Compile your Java program.
  
- `./gradlew installDist`

  Compile your program and place execution scripts in `bin/`
  
- `bin/BankApp` (`bin\BankApp.bat` on Windows)

  Run the `BankApp` program interactively. Requires you to have run
  `installDist` already.
  
- `./gradlew test`

  Compile and run the complete test suite.
  
- `./gradlew test -i`

  Run the test suite with additional diagnostic information.
  
### Making the most of the test suite

I have tried to make the tests and their failure messages as informative as possible. Keep the following in mind when attempting to fix failing tests:

- You will want to fix all failing `Interface` tests first. If the interface of
  your program is incorrect, the test suite will be unable to evaluate other
  behaviors.
- Tests will fail when there is a **failed assertion**. Each assertion compares
  some expected condition to an actual result from your program; when an
  assertion fails, Gradle will indicate what was expected and what was actually
  received.
- Every failing test includes a **stack trace**. This allows you to pinpoint
  which line of code triggered the failing assertion, and potentially trace your
  problem back to its source.
- If you are unable to get enough detail from the test feedback, read the actual
  test source file in `src/test`. You can see which test method failed, and
  where, by referencing the stack trace.
- The provided tests are all **end to end tests**, meaning that they test the
  correctness of the program entirely through its user-level public interface.
  You may want to write your own **unit tests** that test the correctness of
  individual components directly. A few sample unit tests for one possible
  design of the `CheckingAccount` class is included in
  `unitTestSample/CheckingAccountTests.java`.
- The provided test suite is meant to **illustrate** the requirements. It is not
  an exhaustive test suite and does not guarantee the correctness of your
  program. You might want to write your own tests as well!

## Requirements

You will create a Java program, `BankApp`, that allows you to interact with a
collection of bank accounts, as well as create new ones. Your program should
contain, at least, the following classes, `BankApp`, `CheckingAccount`, `Bank`,
`PasswordUtils`. You may create additional classes as you see fit.

The program will be tested based on its user interface, as defined in `BankApp`.
You have more freedom in how you implement the application, but you **must**
ensure that no passwords are saved in plain text.

The program will be invoked with the command `bin/BankApp [ACCOUNTS_FILE]`.

Your program must be able to load accounts from disk and save changes to those
accounts back to disk. A sample `accounts.txt` file is provided with a set of
accounts that you can use. You can reset this file to its original state at any
time with the command `git checkout starter-code accounts.txt`. To allow for
proper testing, the location of the `ACCOUNTS_FILE` must be passed as a
command-line argument.

### Source package

All Java classes should be members of the package `edu.brooklyn.project2`. You
can declare the package for a source file with the line `package
edu.brooklyn.project2;`.

### Grading Criteria

Your program will be graded on its correctness and the quality of its
implementation.

Correctness will be verified using an automated testing suite, so it is
important that you adhere to any specifications given. Correctness includes your
program's behavior under various edge cases and failure states. The test suite
will include all tests provided with the starter code, but may contain other
tests as well.

Quality of implementation is a somewhat more subjective metric. Programs should
be well structured, well written, and well formatted. Try to apply the practices
of object-oriented design that we have studied over the course of the semester.
Your implementation should make reasonably good use of computer resources.

### Expected Behaviors

#### Basic interface

- On launch, your program should print the following menu. Be sure to include a
  space after `Enter option: `, but do not print a new line. (`BankAppTests.accountMenu`)

    ```
    1. Log into bank account
    2. Create bank account
    0. Exit
    Enter option: 
    ```

- When the `0. Exit` option is chosen, your program should print `Exit.` and
  terminate. (`BankAppTests.exitProgram`)
  
- When an unknown option is listed, print "Unknown option <OPTION>." and reprint the appropriate menu. (`BankAppTest.unknownOption`)

- Any parsing errors due to inappropriate input should be handled gracefully.

- Example session:

    ```
    1. Log into bank account
    2. Create bank account
    0. Exit
    Enter option: 5
    Unknown option 5.
    1. Log into bank account
    2. Create bank account
    0. Exit
    Enter option: 0
    Exit.

    ```
    
#### Account access

- The `1. Log into bank account` option should prompt the user for a social
  security number and password. (`BankAppAccountTests.accountLoginInterface`)
  
  ```
  1. Log into bank account
  2. Create bank account
  0. Exit
  Enter option: 1

  SSN: 123456789
  Password:  
  ```
  
- Use the provided `PasswordUtils.readPassword(Scanner input)` method to read a
  password from standard input **without echoing the password back to the
  console**.
  
- Use the provided `PasswordUtils.getPasswordHash(String password)` method to
  compare the provided password with the appropriate account. **Do not attempt
  to compare the plaintext password.** See [Working with
passwords](#working-with-passwords) for more detail.
  
- If the (ssn, password) pair does not correspond to an existing account, print
  `Login incorrect.` and allow the user to try again. Do not indicate whether or
  not the supplied ssn corresponds to an existing bank account.
  (`BankAppAccountTests.accountLoginOneFailure`)
  
  ```
  SSN: 123456555
  Password: 
  Login incorrect.

  SSN:    
  ```

- After three tries, print `3 incorrect attempts.` and return to the main menu.
  (`BankAppAccountTests.accountLoginThreeFailures`)

    ```
    1. Log into bank account
    2. Create bank account
    0. Exit
    Enter option: 1

    SSN: 123456555
    Password: 
    Login incorrect.

    SSN: 123456789
    Password: 
    Login incorrect.

    SSN: 123456789
    Password: 
    Login incorrect.
    3 incorrect attempts.

    1. Log into bank account
    2. Create bank account
    0. Exit
    Enter option: 
    ```
- If the login is successful, print the account management menu. (`BankAppAccountTests.accountMenuInterface`)

  ```
  1. Check balance
  2. Withdraw
  3. Deposit
  0. Exit
  Enter option:   
  ```
  
#### Account management

- The general interface should match the previous menu.

- The `1. Check balance` option should display the current account balance. Be
  sure to include a dollar sign, two digits of precision for cents, and any
  appropriate `,` symbols between groups of digits in large numbers. (e.g.
  `$5,600,000.00`, `$127.28`, `$900,623,000.00`) (`BankAppAccountTests.checkBalance`)

  ```
  1. Check balance
  2. Withdraw
  3. Deposit
  0. Exit
  Enter option: 1
  The balance is $1,000.00

  ```
  
- The `2. Withdraw` option should prompt the user for an amount and then attempt
  to adjust the current balance. (`BankAppAccountTests.makeWithdrawal`)
  
  ```
  1. Check balance
  2. Withdraw
  3. Deposit
  0. Exit
  Enter option: 2
  Enter dollar amount to withdraw: 125.00
 
  1. Check balance
  2. Withdraw
  3. Deposit
  0. Exit
  Enter option: 1
  The balance is $875.00 

  ```
  
  - If the amount to withdraw exceeds the current balance, print `Insufficient
    funds.` and leave the current balance unchanged.
    
  - If the amount to withdraw is zero or negative, print `Amount must be
    positive.` and leave the current balance unchanged.
    
  - If the amount to withdraw contains a currency amount smaller than 1 cent
    (e.g. `25.001`), print `Amount must not contain fractional cents.` and leave
    the current balance unchanged.

- The `3. Deposit` option should prompt the user for an amount and then add that
  to the current balance. (`BankAppAccountTests.makeDeposit`)

  ```
  1. Check balance
  2. Withdraw
  3. Deposit
  0. Exit
  Enter option: 3
  Enter dollar amount to deposit: 125 

  1. Check balance
  2. Withdraw
  3. Deposit
  0. Exit
  Enter option: 1
  The balance is $1,000.00

  ```
  
  - The user should not be able to deposit a negative or zero amount.
  
  - The user should not be able to deposit an amount containing fractional pennies.
  
- The `0. Exit` option should print `Exit.` and return the user to the initial
  menu. (`BankAppAccountTests.accountMenuExit`)

#### Creating accounts

- The `2. Create bank account` option should prompt the user for a social
  security number, new password, and starting balance.
  (`BankAppTests.createAccountInterface`, `BankAppTests.createAccount`)
  
  ```
  1. Log into bank account
  2. Create bank account
  0. Exit
  Enter option: 2
  Create new bank account.

  SSN: 987654321
  Password: 
  Balance: 321.00

  1. Log into bank account
  2. Create bank account
  0. Exit
  Enter option: 
  ```
  
  - The password **must be converted to a hash value** using the
    `PasswordUtils.getPasswordHash(String password)` method before saving. See [Working with
passwords](#working-with-passwords) for more detail.
  - The social security number must contain exactly 9 digits.
  - The social security number should not be in use by any existing bank
    account.
  - The password must not be empty.
  - The starting balance must not be negative or zero.
  
#### Loading and saving accounts on disk

- If a `ACCOUNTS_FILE` (e.g. `accounts.txt`) is provided, `BankApp` must load
  these accounts at startup.
- The accounts file must be of the format `SSN PASSWORD_HASH BALANCE`:

  ```
  123456789 72623278f43d70bd8a9cf704993dd46262648a81f3d1811b70404c630da54b22 1000.00
  123456788 84b8bb4353a42b613bdf64c88f9acbe6725f171ea51f61f0c8262871cfda862f 5000.00
  123456787 ec53367c2a0319021900b35806cfd86833562975890db296ce2344e6b0d786ad 25000.00
  123456786 9d934f1c28f95f2b3a90112066f12001aca77bb0a421fe996b931bb778142c1a 125000.00
  123456785 04dd5b08a1a9265eef5905f16323db7232abc851d1fb39ff207e0652f6fbe6a7 625000.00 
  ```
- There should be **no plaintext passwords** stored in the accounts file.
- The file must be updated with any new accounts or changes to the balances of
  existing accounts.
- If any error occurs while loading the file, the program should report the
  error and exit gracefully.
- If any error occurs while saving the file, the program should report the error
  **without exiting**.

## Guidance

### Structuring your program

While this project gives you greater freedom in how you approach your design
than project 1, the overall structure should be fairly similar. You have at the very least:

- An interface component (`GpaCalculatorClient`, `BankApp`)
  - Contains `main` function
  - Collects user input and makes appropriate queries to a back-end class
  - Formats and displays information from back-end class
- A back-end component (`GpaCalculator`, `Bank`)
  - Aggregates records containing user data
  - Adds records
  - Mediates access to records
- A record class (`Course`, `CheckingAccount`)
  - Aggregates primitive data fields for an individual record
  - Provides basic getters and setters for those fields
- A utilities class (`GradeUtils`, `PasswordUtils`)
  - Provides static methods that translate user input to an internal
    representation
    
Try using the structure of your GPA project as a template and sketch out a
structure for your bank project. Consider some of the operations defined above.
What path would those operations trace through your class structure. e.g., in
which class does creating an account originate? What happens next?

Two methods in `PasswordUtils` are already implemented for you.

You do not need to be limited to the above four classes, nor must you follow the
same design patterns used in project 1. If you think you can improve on that
design, go for it. My implementation, for instance, uses 5 concrete classes and
one abstract class.

### Working with passwords

`PasswordUtils.getPasswordHash(String password)` uses an SHA-256 cryptographic
hash function to deterministically generate a 256-bit string from `password`.
Rather than working with user passwords directly, you will only store the hashed
value of user passwords, and you will authenticate users by comparing the hashed
value of the password they enter to the hashed value that is stored. Read on for
more information.

Whenever working with user data, it is essential to consider issues of security,
especially when that data is something as sensitive as financial information! A
key line of defense in most security systems is the user password, so it is
essential to protect the secrecy of this value.

**Information leakage** occurs when supposedly private information can be
revealed to an outside party, and is surprisingly difficult to guard against.
Any information that is stored has a tendency to leave traces, to be copied from
one location to another, to be unwittingly bundled with a larger, insecure,
transfer. Many of the most famous data leaks involve malicious actors obtaining
large collections of stored passwords. A simple solution presents itself ---
passwords cannot leak if they are never stored in the first place.

Keeping a record of a user's password presents a vulnerability, but how can a
system verify a user's password without having a copy for comparison? One
solution uses a **cryptographic hash function**.

A **hash function** is any function that maps an input of an arbitrary size
(such as a string of characters) to some finite set of values, say, any number
between 0 and $2^{256}$. Hash functions of many applications, and most are not
suitable for security purposes. For security, we are interested in the family
known as **cryptographic hash functions**.

A cryptographic hash function must:
- Be deterministic, i.e. for any $x$, $h(x)$ must always equal $y$.
- Appear completely random, such that, given any $y$, it is nearly impossible to
  deduce the value $x$ such that $h(x) = y$.
- Possess a number of other collision properties that you can read about
  elsewhere!
  
In this scheme, a user's password is securely hashed as soon as the account is
created; that value is stored. Every time the user attempts to login, they
provide their real password, which the system immediately hashes, and then
compares to the stored value. If `h(stored) == h(entered)`, and the hash
function satisfies cryptographic standards, we can state that, with an extremely
high, but not absolute, degree of certainty, that `stored == entered`.

### Working with files

You will store a record of each account in a text file, allowing you to persist
bank data across multiple sessions. As described in [Working with
passwords](#working-with-passwords), you should **never store original
passwords** in this file.

It is important that your file match the format specified in the requirements.

For simplicity, we will load the entire file into memory when launching our
program, and rewrite the entire contents when making changes. What might a more
efficient approach look like? What would you need to implement it?

#### Choosing the accounts file

Each test will need to work with its own, isolated accounts file. This allows
the tests to work independently, and to operate without fear of corrupting
important user data. To allow this, your program should accept the location of
the accounts file as a **command line parameter**, e.g. `bin/BankApp
path/to/my/accounts.txt`.

Command line parameters are passed to your program via the `String[] args`
argument of your main function.

If no accounts file exists, your program should simply create an empty one.

For convenience, you may choose to have the program use a default accounts file,
such as `accounts.txt`, if no arguments are provided.

#### Reading the accounts file

As discussed in our lecture on file IO, you can use the
[`Paths.get(...)`](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html#get-java.net.URI-)
method to create a
[`Path`](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Path.html)
object corresponding to the provided filename. You can
then pass that `Path` object to a new
[`Scanner`](https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html)
object, which can be used to
parse each line of the text file into a `CheckingAccount` object.

A good implementation will include error handling to protect against improperly
formatted files.

Be sure to close the `Scanner` object once you are done with it.

#### Saving changes to the accounts file

While there are more efficient approaches, the simplest way to update your file
on disk is to rewrite the entire file with current account data.

There are several options for writing plain text to a file in Java. One is the
[`PrintWriter`](https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html)
class described in the textbook. You could also use the
[`Files.newBufferedWriter`](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#newBufferedWriter-java.nio.file.Path-java.nio.file.OpenOption...-)
method to create a
[`BufferedWriter`](https://docs.oracle.com/javase/8/docs/api/java/io/BufferedWriter.html)
object.

Be sure to close your writer object once you have finished.

### Representing money in your program

#### Cents

For our purposes, we consider money to be a **discrete value**, composed of
dollars and cents. Our program should not attempt to represent partial cents,
and should no allow them in inputs.

While money is generally represented as a decimal value, we need to exercise caution when working with binary representations of non-integer values. Just as many rational numbers (such as 2/3) can only be approximated in decimal notation, many **different** rational numbers can only be approximated in binary floating points. Try opening `jshell` and performing the following calculation:

```
jshell> 1.0 - 0.9
$4 ==> 0.09999999999999998
```

This is because the double precision binary representation of 0.1 is actually
$7205759403792794 / 2^{56}$!

To avoid rounding errors when working with cents, it is better **not** to use floating point numbers.

One alternative is Java's
[`BigDecimal`](https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html)
class, which provides accurate representations of decimal values with up to 4
billion digits of precision.

#### Formatting large numbers

To ease in reading large numbers, we generally print **grouping symbols** that
break numbers into groups of three digits, e.g. 1,250,512. While you could
perform this formatting by hand, the Java standard library provides powerful
formatting tools that can add these symbols for you. See the
[`Formatter`](https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html)
documentation for more info.
