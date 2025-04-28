# Matrix Calculator for Android

A native Android application that performs matrix operations (addition, subtraction, multiplication, and division) using C++ for calculations and Jetpack Compose for the user interface.

## Project Structure

```
matrix-calculator/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/matrixcalculator/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── MatrixOperations.kt
│   │   │   ├── cpp/
│   │   │   │   └── matrix_operations.cpp
│   │   │   ├── CMakeLists.txt
│   │   │   └── AndroidManifest.xml
│   │   └── ...
│   └── build.gradle
├── eigen/     # Add the Eigen library here
└── ...
```

## Setup Instructions

1. Clone the repository.
2. Download the Eigen library from http://eigen.tuxfamily.org and place it in the `eigen/` directory.
3. Open the project in Android Studio.
4. Build and run the application.

## Usage

1. Enter the dimensions for Matrix A and Matrix B.
2. Input the elements for both matrices.
3. Select the operation you want to perform (Add, Subtract, Multiply, or Divide).
4. Click the "Calculate" button to see the result.

## Notes

- For matrix division, the second matrix (B) must be square and invertible.
- Matrix elements should be entered as space-separated values, with rows separated by newlines.
- The C++ implementation uses the Eigen library for efficient matrix operations.