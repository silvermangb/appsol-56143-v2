#!/usr/bin/python2.7

import sys
import os

f = open(sys.argv[1]+'.txt','a+')

f.write(str(os.getpid())+' '+str(os.getppid())+' '+str(os.getpgrp())+' '+sys.argv[1]+'\n');
