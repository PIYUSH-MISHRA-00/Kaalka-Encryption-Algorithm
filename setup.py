# setup.py
from setuptools import setup, find_packages

setup(
    name='kaalka-package',
    version='0.1',
    packages=find_packages(),
    install_requires=[
        'ntplib', 'math', 'time', 'socket'
    ],
    # Metadata
    author='PIYUSH-MISHRA-00',
    author_email='piyushmishra.professional@gmail.com',
    description='Kaalka encryption library',
    url='https://github.com/PIYUSH-MISHRA-00/Kaalka-Encryption-Algorithm/tree/kaalka-package/kaalka_package',
)
