#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <Eigen/Dense>

using namespace Eigen;
using namespace std;

// Helper function to convert a string representation of a matrix to Eigen::MatrixXd
MatrixXd parseMatrix(int rows, int cols, const string& matrixStr) {
    MatrixXd matrix(rows, cols);
    stringstream ss(matrixStr);

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            double val;
            ss >> val;
            matrix(i, j) = val;
        }
    }

    return matrix;
}

// Helper function to convert an Eigen::MatrixXd to a string representation
string matrixToString(const MatrixXd& matrix) {
    stringstream ss;
    ss.precision(4);  // Set precision for floating-point values

    for (int i = 0; i < matrix.rows(); i++) {
        for (int j = 0; j < matrix.cols(); j++) {
            ss << matrix(i, j);
            if (j < matrix.cols() - 1) {
                ss << " ";
            }
        }
        if (i < matrix.rows() - 1) {
            ss << "\n";
        }
    }

    return ss.str();
}

// JNI Functions

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_matrixcalculator_MatrixOperations_addMatrices(
        JNIEnv* env,
        jobject /* this */,
        jint rowsA, jint colsA, jstring matrixAStr,
        jint rowsB, jint colsB, jstring matrixBStr) {

    const char* matrixAChars = env->GetStringUTFChars(matrixAStr, nullptr);
    const char* matrixBChars = env->GetStringUTFChars(matrixBStr, nullptr);

    string result;

    try {
        if (rowsA != rowsB || colsA != colsB) {
            result = "Error: Matrices must have the same dimensions for addition";
        } else {
            MatrixXd A = parseMatrix(rowsA, colsA, matrixAChars);
            MatrixXd B = parseMatrix(rowsB, colsB, matrixBChars);

            MatrixXd C = A + B;
            result = matrixToString(C);
        }
    } catch (const exception& e) {
        result = string("Error: ") + e.what();
    }

    env->ReleaseStringUTFChars(matrixAStr, matrixAChars);
    env->ReleaseStringUTFChars(matrixBStr, matrixBChars);

    return env->NewStringUTF(result.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_matrixcalculator_MatrixOperations_subtractMatrices(
        JNIEnv* env,
        jobject /* this */,
        jint rowsA, jint colsA, jstring matrixAStr,
        jint rowsB, jint colsB, jstring matrixBStr) {

    const char* matrixAChars = env->GetStringUTFChars(matrixAStr, nullptr);
    const char* matrixBChars = env->GetStringUTFChars(matrixBStr, nullptr);

    string result;

    try {
        if (rowsA != rowsB || colsA != colsB) {
            result = "Error: Matrices must have the same dimensions for subtraction";
        } else {
            MatrixXd A = parseMatrix(rowsA, colsA, matrixAChars);
            MatrixXd B = parseMatrix(rowsB, colsB, matrixBChars);

            MatrixXd C = A - B;
            result = matrixToString(C);
        }
    } catch (const exception& e) {
        result = string("Error: ") + e.what();
    }

    env->ReleaseStringUTFChars(matrixAStr, matrixAChars);
    env->ReleaseStringUTFChars(matrixBStr, matrixBChars);

    return env->NewStringUTF(result.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_matrixcalculator_MatrixOperations_multiplyMatrices(
        JNIEnv* env,
        jobject /* this */,
        jint rowsA, jint colsA, jstring matrixAStr,
        jint rowsB, jint colsB, jstring matrixBStr) {

    const char* matrixAChars = env->GetStringUTFChars(matrixAStr, nullptr);
    const char* matrixBChars = env->GetStringUTFChars(matrixBStr, nullptr);

    string result;

    try {
        if (colsA != rowsB) {
            result = "Error: Number of columns in first matrix must equal number of rows in second matrix";
        } else {
            MatrixXd A = parseMatrix(rowsA, colsA, matrixAChars);
            MatrixXd B = parseMatrix(rowsB, colsB, matrixBChars);

            MatrixXd C = A * B;
            result = matrixToString(C);
        }
    } catch (const exception& e) {
        result = string("Error: ") + e.what();
    }

    env->ReleaseStringUTFChars(matrixAStr, matrixAChars);
    env->ReleaseStringUTFChars(matrixBStr, matrixBChars);

    return env->NewStringUTF(result.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_matrixcalculator_MatrixOperations_divideMatrices(
        JNIEnv* env,
        jobject /* this */,
        jint rowsA, jint colsA, jstring matrixAStr,
        jint rowsB, jint colsB, jstring matrixBStr) {

    const char* matrixAChars = env->GetStringUTFChars(matrixAStr, nullptr);
    const char* matrixBChars = env->GetStringUTFChars(matrixBStr, nullptr);

    string result;

    try {
        if (rowsB != colsB) {
            result = "Error: Second matrix must be square for division (using inverse)";
        } else {
            MatrixXd A = parseMatrix(rowsA, colsA, matrixAChars);
            MatrixXd B = parseMatrix(rowsB, colsB, matrixBChars);

            // Matrix division is A * B^-1
            MatrixXd BInverse = B.inverse();
            MatrixXd C = A * BInverse;
            result = matrixToString(C);
        }
    } catch (const exception& e) {
        result = string("Error: ") + e.what();
    }

    env->ReleaseStringUTFChars(matrixAStr, matrixAChars);
    env->ReleaseStringUTFChars(matrixBStr, matrixBChars);

    return env->NewStringUTF(result.c_str());
}