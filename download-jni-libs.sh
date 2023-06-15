#!/usr/bin/env bash
set -e

GATEWAY_REPO_URL="https://github.com/cloudflare/privacy-gateway-client-library"
TAG=$1 # tag of privacy-gateway-client-library to build. e.g. v0.0.4

echo "Building with privacy-gateway-client-library tag $TAG"

LIB_DIR="lib"
BUILD_DIR="${LIB_DIR}/build"

LIBS_FILE="jniLibs.tar.gz"
JNI_LIBS_URL="${GATEWAY_REPO_URL}/releases/download/${TAG}/${LIBS_FILE}"
JNI_LIBS_DOWNLOADED_ARCHIVE="${BUILD_DIR}/${LIBS_FILE}"
echo "Downloading JNI libs from ${JNI_LIBS_URL}"
mkdir -p ${BUILD_DIR}
curl "${JNI_LIBS_URL}" -L -o "${JNI_LIBS_DOWNLOADED_ARCHIVE}"

JNI_LIBS_DIR="${LIB_DIR}/src/main/jniLibs"
mkdir -p ${JNI_LIBS_DIR}
echo "Unpacking JNI libs into ${JNI_LIBS_DIR}"
tar --extract --gzip --verbose --file="${JNI_LIBS_DOWNLOADED_ARCHIVE}" -C ${JNI_LIBS_DIR}
rm ${JNI_LIBS_DOWNLOADED_ARCHIVE}
