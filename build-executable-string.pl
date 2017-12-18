#!/usr/bin/perl -w
use strict ;

my $treeStart = 0 ;
my $treeProcessed = 0 ;
my $homeDrive = $ENV{"HOMEDRIVE"} || die "HOMEDRIVE not set" ;
#print "HomeDrive: $homeDrive\n" ;
my $homePath = $ENV{"HOMEPATH"} || die "HOMEPATH not set";
my $m2repository = "$homeDrive$homePath\\.m2\\repository" ;
#print "repository: $m2repository\n" ;
my @jars = () ;
while(<>) {
	if( $treeStart && /------------------------------------------------------------------------/) {
		$treeProcessed = 1 ;
		#print "SEPARATOR\n" ;
	}
	if($treeStart && !$treeProcessed) {
	
		#print ;
		chomp;

		s/\[INFO\][\\|\s+-]*//;
		if(/([^:]+):([^:]+):([^:]+):([^:]+)/ && !/test$/) {
			my $groupId = $1 ;
			my $artifactId = $2 ;
			my $packaging = $3 ;
			my $version = $4 ;
			$groupId =~ s/[.]/\\/g ;
			#c:\Users\User\.m2\repository\fdv-maven-project-version-updateer\fdv-maven-project-version-updateer\0.0.1-SNAPSHOT\fdv-maven-project-version-updateer-0.0.1-SNAPSHOT.jar 
			#[INFO] fdv-maven-project-version-updateer:fdv-maven-project-version-updateer:jar:0.0.1-SNAPSHOT
			#fdv-maven-project-version-updateer fdv-maven-project-version-updateer  jar 0.0.1-SNAPSHOT
			my $jarPath = "$m2repository\\$groupId\\$artifactId\\$version\\$artifactId-$version.$packaging" ;
			#print "jar: $jarPath\n" ;
			#print " version: $version\n" ;
			#print " packaging: $packaging\n";
			push @jars, $jarPath ;
		}
	}
	if(/maven-dependency-plugin/) {
		$treeStart = 1 ;
	}
}
my $jars = join ";", @jars ;
#print "$jars\n";
my $executable = "start \"\" javaw.exe -DfileEncoding=UTF-8 -classpath $jars com.github.fdvmavenprojectversionupdater.UpdateVersionWindow \%*\n" ;
print "$executable\n";
