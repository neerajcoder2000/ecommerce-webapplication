package com.neeraj.domain;

public enum AccountStatus {
	
	PENDING_VERIFICATION, //Account is created but not yet verified
	ACTIVE,                //Account is active and good standing
	SUSPENDED,             // Account is temporarily suspended due to violation
	DEACTIVATED,           // Account is deactivated user may have chosen to deactivated 
	BANNED,                // Account is parmanent banned due to server violation
	CLOSED                 // Account us parmanent closed possibly at user request 

}
