cmake_minimum_required(VERSION 3.10.2)

project("matrixcalculator")

# Add Eigen library
include_directories(${CMAKE_SOURCE_DIR}/../../../../eigen)

# Create the shared library
add_library(
        matrix-operations
        SHARED
        matrix_operations.cpp
)

# Find the required libraries
find_library(
        log-lib
        log
)

# Link the library with Android log library
target_link_libraries(
        matrix-operations
        ${log-lib}
)