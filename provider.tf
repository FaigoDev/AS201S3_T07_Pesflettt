provider "google" {
credentials = "${file("credentials.json")}"
project     = "symbolic-bit-353221" # REPLACE WITH YOUR PROJECT ID
region      = "us-central1"
}