#!/usr/bin/env python
# -*- coding: utf-8 -*-
#-------------------------------------------------------------------------------

import sys
from os import walk
from os.path import join as joinpath
from zlib import crc32

#-------------------------------------------------------------------------------
if __name__ == '__main__':
    if len(sys.argv) < 2:
        print '%s <rep>' % (sys.argv[0])
        sys.exit(1)
    
    for topdir in sys.argv[1:]:
        for root, dirs, filenames in walk(topdir):
            for filename in filenames:
                filepath = joinpath(root, filename)
                cksum = 0
                with open(filepath, 'rb') as fobj:
                    chunk = fobj.read(8192)
                    while chunk:
                        cksum = crc32(chunk, cksum) & 0xffffffff
                        chunk = fobj.read(8192)
                print filepath, cksum
    
