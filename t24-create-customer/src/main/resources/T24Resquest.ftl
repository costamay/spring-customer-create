<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:t24="http://temenos.com/T24KCBAccountOpening" xmlns:cus="http://temenos.com/CUSTOMERKCBCHANNELS">
   <soapenv:Header/>
	<soapenv:Body>
		<t24:KCBCustomerCreation>
			<WebRequestCommon>
				<company>${body.transactionInfo.companyCode}</company>
				<password>${headers.vaultPassword}</password>
				<userName>${headers.vaultUser}</userName>
			</WebRequestCommon>
			<OfsFunction>
			</OfsFunction>
			<CUSTOMERKCBCHANNELSType id="">
				<cus:Mnemonic>${body.transactionInfo.mnemonic}</cus:Mnemonic>
				<cus:gSHORTNAME g="1">
					<cus:ShortName>${body.transactionInfo.firstName}</cus:ShortName>
				</cus:gSHORTNAME>
				<cus:gNAME1 g="1">
					<cus:Name>${body.transactionInfo.firstName} ${body.transactionInfo.middleName} ${body.transactionInfo.lastName}</cus:Name>
				</cus:gNAME1>
				<cus:gSTREET g="1">
					<cus:Street>${body.transactionInfo.street}</cus:Street>
				</cus:gSTREET>
				<cus:gTOWNCOUNTRY g="1">
					<cus:TownCountry>${body.transactionInfo.town}</cus:TownCountry>
				</cus:gTOWNCOUNTRY>
				<cus:Sector>${body.transactionInfo.sectorCode}</cus:Sector>
				<cus:BranchCode>${body.transactionInfo.branchCode}</cus:BranchCode>
				<cus:Industry>${body.transactionInfo.industryCode}</cus:Industry>
				<cus:Target>${body.transactionInfo.targetCode}</cus:Target>
				<cus:Nationality>${body.transactionInfo.nationality}</cus:Nationality>
				<cus:CustomerStatus>${body.transactionInfo.customerStatus}</cus:CustomerStatus>
				<cus:Residence>${body.transactionInfo.residence}</cus:Residence>
				<cus:gLEGALID g="1">
					<#list body.transactionInfo.documentDetails as documentDetails>
						<cus:mLEGALID m="${documentDetails?counter}">
							<cus:LegalID>${documentDetails.documentNumber}</cus:LegalID>
							<cus:LegalDocName>${documentDetails.documentHolderName}</cus:LegalDocName>
							<cus:LegalHolderName>${documentDetails.documentHolderName}</cus:LegalHolderName>
							<cus:LegalIssueAuth>${documentDetails.issuingAuthority}</cus:LegalIssueAuth>
							<cus:LegalIssueDate>${documentDetails.issueDate}</cus:LegalIssueDate>
							<cus:LegalExpiryDate>${documentDetails.expirtyDate}</cus:LegalExpiryDate>
						</cus:mLEGALID>
					</#list>
				</cus:gLEGALID>
				<cus:Language>1</cus:Language>
				<cus:gPHONE1 g="1">
					<#list body.transactionInfo.contactDetails as contactDetails>
						<cus:mPHONE1 m="${contactDetails?counter}">
							<cus:PhoneNumber>${contactDetails.mobileNumber}</cus:PhoneNumber>
							<cus:Email>${contactDetails.emailAddress}</cus:Email>
						</cus:mPHONE1>
					</#list>
				</cus:gPHONE1>
				<cus:KCBSector>${body.transactionInfo.kcbSector}</cus:KCBSector>
				<cus:CustomerSegment>${body.transactionInfo.customerSegmentCode}</cus:CustomerSegment>
				<cus:gKCB.DOC.NAME g="1">
					<#list body.transactionInfo.documentDetails as documentDetails2>
						<cus:mKCB.DOC.NAME m="${documentDetails2?counter}">
							<cus:KCBDocName>${documentDetails2.documentHolderName}</cus:KCBDocName>
							<cus:KCBDocID>${documentDetails2.documentNumber}</cus:KCBDocID>
							<cus:DocumentIssueAuthority>${documentDetails2.issuingAuthority}</cus:DocumentIssueAuthority>
							<cus:DocumentIssueDate>${documentDetails2.issueDate}</cus:DocumentIssueDate>
							<cus:DocumentExpiryDate>${documentDetails2.expirtyDate}</cus:DocumentExpiryDate>
						</cus:mKCB.DOC.NAME>
					</#list>
				</cus:gKCB.DOC.NAME>
			</CUSTOMERKCBCHANNELSType>
		</t24:KCBCustomerCreation>
	</soapenv:Body>
</soapenv:Envelope>