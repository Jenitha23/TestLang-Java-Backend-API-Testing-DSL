java -jar lib\jflex-full-1.9.1.jar -d src/main/java src/main/jflex/scanner.flex
# 1. generate parser
java -jar "lib\java-cup-11b.jar" -parser Parser -symbols sym -destdir src/main/java -expect 2 src/main/cup/parser.cup
# 1. Create AST classes first
mkdir out 2>nul

# 2. Compile AST classes
javac -cp "lib\java-cup-11b-runtime.jar" -d out src/main/java/com/testlang/ast/*.java

# 3. Compile lexer and parser
javac -cp "out;lib\java-cup-11b-runtime.jar" -d out src/main/java/com/testlang/lexer/*.java
javac -cp "out;lib\java-cup-11b-runtime.jar" -d out src/main/java/com/testlang/parser/*.java

# 4. Compile codegen and driver
javac -cp "out;lib\java-cup-11b-runtime.jar" -d out src/main/java/com/testlang/codegen/*.java
javac -cp "out;lib\java-cup-11b-runtime.jar" -d out src/main/java/com/testlang/Driver.java

echo All files compiled successfully!
# Generate JUnit tests from your example.test file
java -cp "out;lib\java-cup-11b-runtime.jar" com.testlang.Driver examples/example.test examples/GeneratedTests.java

# Compile the generated tests (you'll need JUnit 5 in classpath)
javac -cp "examples;out;lib\java-cup-11b-runtime.jar;lib\junit-platform-console-standalone-1.10.1.jar" examples\GeneratedTests.java

# Run the tests
java -jar lib\junit-platform-console-standalone-1.10.1.jar --class-path "examples;out;lib\java-cup-11b-runtime.jar" -c GeneratedTests

# Verify all components work:
cd dsl-core

# 1. Test parser on valid input
java -cp "out;lib\java-cup-11b-runtime.jar" com.testlang.Driver examples/example.test examples/GeneratedTests.java

# 2. Test parser on invalid input (should error)
java -cp "out;lib\java-cup-11b-runtime.jar" com.testlang.Driver examples/invalid1.test examples/InvalidTests.java

# 3. Check generated file exists and looks correct
type examples\GeneratedTests.java