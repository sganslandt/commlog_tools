#! /usr/bin/perl
use strict;

my %requests = ();
my %requestTimes = ();
my $requestTimestamp;
my $responseTimestamp;
my $responseTime;
my $requestId;

my $hours = 60*60*1000;
my $minutes = 60*1000;
my $seconds = 1000;
my $ms = 1;

while (<>) {
	
	if ($_ =~ m/\d{4}-\d{2}-\d{2} (\d{2}):(\d{2}):(\d{2}),(\d{1,3}) .* Request: \[(.*)\] .*/) {
		$requestTimestamp = $1 * $hours + $2 * $minutes + $3 * $seconds + $4 * $ms;
		$requests{$5} = $_;
		$requestTimes{$5} = $requestTimestamp;
	} elsif ($_ =~ m/\d{4}-\d{2}-\d{2} (\d{2}):(\d{2}):(\d{2}),(\d{1,3}) .* Response: \[(.*)\] .*/) {
		$responseTimestamp = $1 * $hours + $2 * $minutes + $3 * $seconds + $4 * $ms;
		$responseTime = $responseTimestamp - $requestTimes{$5};
		$requestId = $5;
		$requests{$5} =~ m/\d{4}-\d{2}-\d{2} (\d{2}:\d{2}:\d{2}),\d{1,3} .*/;
		delete $requests{$requestId};
		print "$1 $requestId $responseTime " . keys (%requests) . "\n";
	} else {
		print STDERR "Could not parse: " . $_ . "\n";
	}
	
}
