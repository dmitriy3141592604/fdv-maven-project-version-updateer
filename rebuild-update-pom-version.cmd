call maven dependency:tree > tree.tree
perl build-executable-string.pl tree.tree > update-pom-version.cmd
copy update-pom-version.cmd c:\wks\bin
