# Advanced Screenshot Utility

A powerful desktop application built with JavaFX for capturing screen regions, recognizing text (OCR), and scanning QR codes.

## Features

- **Custom Region Capture**: Select any rectangular area on the screen.
- **Interactive Selection**: Easily move and resize the selection area before capturing.
- **Text Recognition (OCR)**: Extracts text from images using the Tesseract OCR Engine.
- **QR Code Scanning**: Automatically detects and decodes QR codes using the ZXing library.

---

## 1. Prerequisites

Before you begin, ensure you have the following installed on your system.

### System Checks

You can verify the installations by running these commands in your terminal:

- **Check for Java (JDK 17+):**
  ```bash
  java -version
  ```

- **Check for Maven:**
  ```bash
  mvn -version
  ```

- **Check for Tesseract:**
  ```bash
  tesseract --version
  ```
  If Tesseract is not found, you must install it by following the steps below.

---

## 2. Installation (Ubuntu/Debian)

These instructions are tailored for Ubuntu/Debian systems. For other operating systems, please refer to their respective package managers or official websites.

1.  **Update Package List:**
    ```bash
    sudo apt update
    ```

2.  **Install Java and Maven:**
    ```bash
    sudo apt install openjdk-17-jdk maven
    ```

3.  **Install Tesseract and Required Libraries:**
    The application requires Tesseract, its development libraries (for JNA), and language data packs.

    ```bash
    # Install Tesseract engine, development files, and English/Vietnamese language packs
    sudo apt install tesseract-ocr libtesseract-dev tesseract-ocr-eng tesseract-ocr-vie
    ```
    *   `libtesseract-dev` is crucial as it provides the native libraries (`.so` files) that Tess4J/JNA needs to link against.

---

## 3. Building and Running

### Clone the Repository
```bash
git clone <your-repository-url>
cd <repository-folder>
```

### Build the Application
Use Maven to compile the code and package it into an executable JAR file.

```bash
mvn clean package
```
The final artifact (e.g., `shoot.jar`) will be located in the `target/` directory.

### Run the Application
```bash
java -jar target/shoot.jar
```

---

## 4. Development & Maven Commands

Here are some common Maven commands for development:

- **Clean the project** (deletes the `target` directory):
  ```bash
  mvn clean
  ```

- **Compile the source code:**
  ```bash
  mvn compile
  ```

- **Run unit tests:**
  ```bash
  mvn test
  ```

- **Run the application directly without packaging:**
  ```bash
  mvn javafx:run
  ```

- **Clean, build, and install** the artifact into your local Maven repository:
  ```bash
  mvn clean install
  ```

---

## ⚠️ Important: Hardcoded Paths

The current version of the application contains **hardcoded paths** in `src/main/java/com/andy/screen/shoot/utils/OCRUtils.java`.

```java
// Hardcoded path for JNA native library (Linux-specific)
System.setProperty("jna.library.path", "/usr/lib/x86_64-linux-gnu");

// Hardcoded path for Tesseract data files
tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata");
```

This means the application, in its current state, will **only run correctly on Debian/Ubuntu-based systems** with this exact directory structure. It will fail on Windows, macOS, or other Linux distributions. For a more robust solution, these paths should be configurable or discovered dynamically.

---

## Key Libraries Used

- **JavaFX**: UI Framework
- **Tess4J**: Java wrapper for Tesseract OCR
- **ZXing**: QR Code and barcode processing
- **ControlsFX**: Additional UI controls for JavaFX
