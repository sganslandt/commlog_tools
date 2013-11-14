#! /usr/bin/perl
use strict;

my %reqsPerMinute = ();


while (<>) {

	# filter requests
	# 2011-04-27 08:00:14,085 INFO  [EvolutionAdapterComm] (ajp-0.0.0.0-8009-3) Request: [e6994183-49eb-49d9-b55c-85b99d58d713] getUserBalance({sid=3jKXN3wJ4Ws5hm1B9wGDq68zVYmNJ2YyHs0Gkp02hvB2L0x4cXh8!-1645582456!1303884009966})
	# 2011-04-27 08:00:14,097 INFO  [EvolutionAdapterComm] (ajp-0.0.0.0-8009-3) Response: [e6994183-49eb-49d9-b55c-85b99d58d713] getUserBalance({balance=221.27, currency=SEK})
	
	if ($_ =~ m/\d{4}-\d{2}-\d{2} (\d{2}:\d{2}):\d{2},\d{1,3} .* Request: .*/) {
		# put +1 in hashmap on minute
		# $hash{$key} = $value;
		$reqsPerMinute{$1} += 1;
	}
	
}

# print the hashmap
for (keys %reqsPerMinute) {
	print "$_,$reqsPerMinute{$_}\n";
}
