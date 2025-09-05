
# setup.py for Kaalka: Production-ready time-based encryption library
from setuptools import setup, find_packages

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name='kaalka',
    version='5.0.0',
    packages=find_packages(),
    install_requires=[
        'ntplib'
    ],
    entry_points={
        'console_scripts': [
            'kaalka-time-cli=cli.kaalka_time_cli:main'
        ]
    },
    author='PIYUSH-MISHRA-00',
    author_email='piyushmishra.professional@gmail.com',
    description='Kaalka: Production-ready time-based encryption library for Python',
    long_description=long_description,
    long_description_content_type="text/markdown",
    url='https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm/tree/python',
    license='Custom',
)
