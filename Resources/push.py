import os
from os.path import expanduser
home = expanduser("~")
to_cd = home + "/Desktop/newsmapper.github.io"
os.chdir(to_cd)

auto_pull = input('Auto-pull now? (y/anything): ')
if auto_pull is 'y':
	os.system("git pull https://github.com/NewsMapper/newsmapper.github.io")
auto_commit = input('Commit message (type "q" to quit): ')
if auto_commit is 'q':
	raise SystemExit

files = os.listdir()
for foo in files:
	os.system("git add " + foo)

os.system("git commit -m '" + auto_commit.strip("'") + "'")
os.system("git remote rm origin")
os.system("git remote add origin https://github.com/NewsMapper/newsmapper.github.io")
os.system("git push origin master")
