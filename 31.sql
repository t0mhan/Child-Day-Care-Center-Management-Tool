
/***********************************************************selection of activity by parent AND assignment of provider to that activity by admin(for now that association is maintained in two tables 1.Bridgetabe(child_provider and 2.report table))*/

CREATE TABLE `report` (
  fk_idchild INT unsigned NOT NULL ,
  fk_idactivity INT unsigned NOT NULL,
  fk_idprovider  INT unsigned ,
  fk_idsession  INT unsigned  NOT NULL,
  `MON` INT(3) NULL,
  `TUE` INT(3) NULL,
  `WEN` INT(3) NULL,
  `THU` INT(3) NULL,
  `FRI` INT(3) NULL,
  CONSTRAINT report.fk_idsession FOREIGN KEY(fk_idsession)  REFERENCES chcare.day_session(idsession),
  CONSTRAINT report.fk_idprovider FOREIGN KEY(fk_idprovider)  REFERENCES chcare.care_provider(idcare_provider),
  FOREIGN KEY report(fk_idchild) REFERENCES child_info(idchild) ON DELETE no action,
  FOREIGN KEY report(fk_idactivity) REFERENCES activity(idactivity) ON DELETE no action,
  PRIMARY KEY(fk_idchild,fk_idsession)
  )ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

/***********************************************************selection of activity by parent AND assignment of provider to that activity by admin(for now that association is maintained in two tables 1.Bridgetabe(child_provider and 2.report table))*/

2. Get All appropriate Activities available for the child of a parent after parent gets logged in and then select the activitz for child?
	
 /*--Authenticatin:--
     save "parentid" so that it can be used throughout the session */
  SET @parentid=(select idparent from login where username=? and pass=?);

 /*--retrival--*/
  SET @childid=(select fk_idchild from parent where idparent=@parentid);
  
  
  select a.idactivity as activityId, a.activity_name,ag.idage_group as agegroupId,ag.name as ageGroupName,ds.idsession as sessionId,ds.session_name, a.activity_description    
  from age_group ag join activity a join day_session ds
  on(a.fk_age_group=ag.idage_group and a.fk_session=ds.idsession)
  where a.fk_age_group=(select ag1.idage_group 
                        from age_group ag1 
						where ag1.idage_group=(select ch.fk_age_group 
						                       from child_info ch 
											   where idchild=@childid))
  
  /*OR*/
  Get All appropriate Activities available for the child of a parent by providing age group of the child?
  
  select a.idactivity as ActivityId, a.activity_name,ag.name,a.fk_session as SessionID,ds.session_name,a.activity_description
  from activity a join age_group ag join day_session ds
  ON(a.fk_age_group=ag.idage_group and ds.idsession=a.fk_session)
  where fk_age_group=3;

  /*THEN parent will assign the activity and session */
  
    SET @childid=(select idchild from parent where idparent=?);
    /*Note******** accurate selection of Activity in a perticualar session is 'very very' important parent must select only those activity which is availabe for that age group in that session*/
	insert into report(fk_idchild,fk_idactivity,fk_idsession)
	values(@childid,13,102),(@childid,16,101),(@childid,17,103);/*take remaining two field from console*/

3.ADMIN> assign the child?provider association in BRIDGE table?

  
   /*Admin will check the CareProviders available with each Activity for a perticular ageGroup*/
    select a.idactivity as ActivityId, a.activity_name,ag.name as ageGroup,a.fk_session as SessionID,ds.session_name,cp.idcare_provider as IDcareProvider,cp.name as CareProvider,a.activity_description
    from activity a join age_group ag join day_session ds join care_provider cp
    ON(a.fk_age_group=ag.idage_group and ds.idsession=a.fk_session and cp.idcare_provider=a.fk_idcareprovider)
    where fk_age_group=3;   
 
	 
   /*Admin will provide the child info and provider info to the procedure and association in bridge table will be done*/
  create procedure CareProviderChildAssociation(childName VARCHAR(40),childSurname VARCHAR(40),dob date,careProviderEmailId VARCHAR(50),careProviderName varchar(40))
	
	BEGIN
    SET @child_id =(select idchild from child_info where name=childName and surname=childSurname and dob=dob);
	SET @careprovider_id =(select idcare_provider from care_provider where careProviderName=careProviderName and emailid=careProvideremailId);

	select @child_id,@careprovider_id from dual;
	IF(@child_id!=0 AND @careprovider_id!=0) THEN     
	INSERT IGNORE INTO testchildcare.child_vs_careprovider(fk_idchild,fk_idcare_provider) VALUES(@child_id , @careprovider_id);
	ELSE 
	  select 1+1 from dual;
	END IF;
    END$$
	DELIMITER ;

	CALL CareProviderChildAssociation('anil','ambani','1991-04-02','sara@gmail.com','sara');
   /*Now admin will update the report table where he will put provider id for that perticular(activity and session)*/
   /*Note******** accurate selection of Activity ,session and providerId is 'very very' importanat*/
	
    drop procedure if exists CareProviderChildAssociation1; 
    DELIMITER $$
    create procedure CareProviderChildAssociation1(activityId INT, sessionId INT, providerId INT)
    BEGIN
    UPDATE report
    SET    fk_idprovider=providerId
    WHERE  fk_idactivity=activityId and fk_idsession=sessionId;
    END$$
    DELIMITER ;
    CALL CareProviderChildAssociation1(13,101,1);