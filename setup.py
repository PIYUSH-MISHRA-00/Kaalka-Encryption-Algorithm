# setup.py
from setuptools import setup, find_packages

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name='kaalka',
    version='2.0',
    packages=find_packages(),
    install_requires=[
        'ntplib'
    ],
    # Metadata
    author='PIYUSH-MISHRA-00',
    author_email='piyushmishra.professional@gmail.com',
    description='Kaalka Encryption Library',
    long_description=long_description,
    long_description_content_type="text/markdown",
    url='https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm/tree/python',
)
