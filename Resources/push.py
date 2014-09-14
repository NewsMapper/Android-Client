import os

to_cd = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(to_cd)

auto_pull = input('Auto-pull now? (y/anything): ')
if auto_pull is 'y':
	os.system("git pull https://github.com/NewsMapper/Android-Client")
auto_commit = input('Commit message (type "q" to quit): ')
if auto_commit is 'q':
	raise SystemExit

os.system("git add .")

os.system("git commit -m '" + auto_commit.strip("'") + "'")
os.system("git remote rm origin")
os.system("git remote add origin https://github.com/NewsMapper/Android-Client")
os.system("git push origin master")
