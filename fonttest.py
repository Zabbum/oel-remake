import sys
import fontforge
f = fontforge.open(sys.argv[1])
i = f['i']
m = f['m']

if i.width == m.width:
    print('Monospace!')
