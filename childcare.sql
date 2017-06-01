use child_care;

1. create age group?
ANS: 

CREATE TABLE `child_care`.`age_group` (
  `idage_group` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `min_age` INT UNSIGNED NOT NULL,
  `max_age` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`idage_group`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

2. create parent table?
ANS:

CREATE TABLE `child_care`.`parent` (
  `idparent` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `indx_idparent` (`idparent` ASC),
  PRIMARY KEY (`idparent`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

insert into parent(name,surname)
value('Dhirubhai','Ambani'),
     ('Suresh','Kulkarni');

3. create child info table ?/*age group and parent tables should be created before this table*/
ANS: 

CREATE TABLE `child_care`.`child_info` (
  `idchild` INT(3) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `age` INT UNSIGNED NOT NULL,
  `fk_age_group` INT UNSIGNED NOT NULL,
  `fk_idparent` INT  UNSIGNED NOT NULL,
  PRIMARY KEY (`idchild`),
  FOREIGN KEY child_info(fk_age_group) references  age_group(idage_group) on update cascade,
  CONSTRAINT child_info.fk_idparent FOREIGN KEY(fk_idparent) references  parent(idparent) on update cascade,
  UNIQUE INDEX `indx_child_id` (`idchild` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

insert into child_info(name,surname,dob,age,fk_age_group,fk_idparent)
value('mukesh','Ambani','1991-04-02',26,3,1),
('Anil','Ambani','1991-04-02',26,3,1);

4. create contact table?
CREATE TABLE `child_care`.`contact` (
  `idcontact` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `street` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `pincode` INT UNSIGNED NOT NULL,
  `phone_number` VARCHAR(45) NULL,
  `emailid` VARCHAR(50) NULL,
  `fk_idparent` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`idcontact`),
  FOREIGN KEY (fk_idparent) REFERENCES parent(idparent) ON DELETE CASCADE,
  UNIQUE INDEX `indx_idcontact` (`idcontact` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

insert into contact(street,city,pincode,phone_number,emailid,fk_idparent)
values('lk street 37','Mumbai',41234,'73674986501','dhirubhai@gmail.com',1),
      ('henkel Teodral str 7','Heidelberg',65212,'345-908022143','suresh@gmail.com',2);
      
5. create food table?

CREATE TABLE `child_care`.`food` (
  `day` CHAR(3) NULL,
  `breakfast` VARCHAR(50) NULL,
  `lunch` VARCHAR(50) NULL,
  `snak` VARCHAR(50) NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


6.   create activity,material table?/*now activity is connected to provider,session,ageGroup(many(activities)-one(provider));before creating this table create age_group,day_session,rating,care_provider tables.*/
    ANS:
	
	 
  CREATE TABLE `childcare`.`activity` (
	  `idactivity`     INT UNSIGNED NOT NULL AUTO_INCREMENT,
	  `activity_name`  VARCHAR(45) NOT NULL,
      `fk_age_group`   INT UNSIGNED,
      `fk_session`     INT UNSIGNED,
      `fk_idcareprovider`  INT UNSIGNED,
      `activity_description` VARCHAR(400),
	  PRIMARY KEY (`idactivity`),
       CONSTRAINT fk_age_group FOREIGN KEY(fk_age_group)  REFERENCES age_group(idage_group) on update cascade,
       CONSTRAINT fk_session FOREIGN KEY(fk_session)  REFERENCES day_session(idsession) on update cascade,
	   CONSTRAINT fk_idcareprovider FOREIGN KEY(fk_idcareprovider)  REFERENCES care_provider(idcare_provider) on update cascade,
      UNIQUE INDEX `indx_idactivity` (`idactivity` ASC))
	  ENGINE = InnoDB
	  DEFAULT CHARACTER SET = utf8
	  COLLATE = utf8_general_ci;
	   
	 CREATE TABLE `child_care`.`material` (
	  `idmaterial` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	  `material_name` VARCHAR(45) NOT NULL,
	  PRIMARY KEY (`idmaterial`))
	  ENGINE = InnoDB
	  DEFAULT CHARACTER SET = utf8
	  COLLATE = utf8_general_ci;  
    
	/* Bridge table for many2many mapping*/
	CREATE TABLE `child_care`.`activity_vs_material` (
    fk_idmaterial INT UNSIGNED NOT NULL ,
    fk_idactivity INT UNSIGNED NOT NULL ,
	CONSTRAINT activity_vs_material.fk_idmaterial FOREIGN KEY(fk_idmaterial)  REFERENCES child_care.material(idmaterial),
    CONSTRAINT activity_vs_material.fk_idactivity FOREIGN KEY(fk_idactivity)  REFERENCES child_care.activity(idactivity),
    
    primary key pk_activity2material(fk_idmaterial,fk_idactivity)
   )ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;  

7.0  create ratings table for provider?/*provider and parent must be created befor creating this table a parent can enter rate for a provider only once but he can rate many providers(he should rate only to provider his child is dealing with)*/
    ANS:             CREATE TABLE rating( 
	                 fk_idprovider INT UNSIGNED  NOT NULL,
                     rate TINYINT CHECK (rate BETWEEN 1 and 5),
                     fk_idparent INT UNSIGNED,
                     primary key(fk_idprovider,fk_idparent),
                     CONSTRAINT fk_idprovider FOREIGN KEY(fk_idprovider) REFERENCES care_provider(idcare_provider),
					 CONSTRAINT fk_idparent FOREIGN KEY(fk_idparent) REFERENCES parent(idparent))
				     ENGINE = InnoDB
	                 DEFAULT CHARACTER SET = utf8
	                 COLLATE = utf8_general_ci;	
     /*insert into rating values(3,3,1),
        (1,5,1),
        (2,5,1),(6,5,1);*/	
		
7.  create careprovider/*one(Provider)-many(activities)*/
    ANS:

    CREATE TABLE `child_care`.`care_provider` (
  `idcare_provider` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `emailid` VARCHAR(50) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
   PRIMARY KEY (`idcare_provider`))
   ENGINE = InnoDB
   DEFAULT CHARACTER SET = utf8;
  ALTER TABLE `testchildcare`.`care_provider` 
  ADD UNIQUE INDEX `indx_idcareprovider` (`idcare_provider` ASC);		
   /*insert into  care_provider(name,emailid,phone_number) 
		values('sara','sara@gmail.com','0424-1234556'),
		('karo','karo@gmail.com','0786-12313556'),
		('anna','anna@gmail.com','0424-76887658'),
		('jina','jina@gmail.com','0786-12324624556'),
		('christiana','christiana@gmail.com','0424-1234656556'),
		('hessel','hessel@gmail.com','0786-1234576756'); 
	*/
	
 /* Bridge table for many2many mapping*/
8. create child_vs_careprovider
  
  CREATE TABLE `child_care`.`child_vs_careprovider` (
    fk_idchild INT UNSIGNED NOT NULL ,
    fk_idcare_provider INT UNSIGNED NOT NULL ,
    CONSTRAINT child_vs_careprovider.fk_idchild FOREIGN KEY(fk_idchild) REFERENCES child_info(idchild),
    CONSTRAINT child_vs_careprovider.fk_idcare_provider FOREIGN KEY(fk_idcare_provider) REFERENCES care_provider(idcare_provider),
    primary key pk_child2provider(fk_idchild,fk_idcare_provider)
   )ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
     COLLATE = utf8_general_ci;	
	 
9. create common plan?

    CREATE TABLE `child_care`.`common_plan` (
  `plantime` VARCHAR(20) NOT NULL,
  `plan` VARCHAR(45) NOT NULL)
   ENGINE = InnoDB
   DEFAULT CHARACTER SET = utf8;
   
   insert into common_plan(plantime,plan)
values('7:00am','DaycareOpen'),
('7:00am-8:00am','ChildrendroffOff'),
('8:00am-8:30am','Breakfast'),
('8:30am-9:00am','Breakfastclanup and playtime'),
('9:00-9:15am','circletime(MorningSong,Seasonsong etc.'),
('9:15-10:00am','LessonTime/Manipulative/puzzels'),
('10:00-10:30am','Recess Time'),
('10:30-11:00am','Creativecoloring/art time'),
('11:00-11:30am','LunchTime'),
('11:30-12:00pm','Cleanup Time'),
('12:00-2:00pm','Nap Time'),
('2:00-2:30pm','SnakTime'),
('3:00-3:30pm','Music/Story'),
('3:30-4:30pm','play(Bocks/Lego/creativeplay)'),
('4:30-5:00pm','cleanup dissmissal preparation'),
('5:00-5:30pm','Parent pickup');

10.  create moring,afternoon,evening_activity plan which is accesible by parent for customisation?
ANS: /*parent will have access to three activity tables morning,afetrnoon and evening to add activities for his child there is no time but consider activities are conducted 3 times a day*/
   
  /* CREATE TABLE `child_care`.`morning_activity` (
  `fk_idchild` INT UNSIGNED NOT NULL ,
  `MON` VARCHAR(50),
  `TUE` VARCHAR(50),
  `WEN` VARCHAR(50)  ,
  `THU` VARCHAR(50)  ,
  `FRI` VARCHAR(50) , 
   foreign key  morning_activity(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE `child_care`.`afternoon_activity` (
  `fk_idchild` INT UNSIGNED NOT NULL ,
  `MON` VARCHAR(50),
  `TUE` VARCHAR(50),
  `WEN` VARCHAR(50)  ,
  `THU` VARCHAR(50)  ,
  `FRI` VARCHAR(50) , 
   foreign key  morning_activity(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE `child_care`.`evening_activity` (
  `fk_idchild` INT UNSIGNED NOT NULL ,
  `MON` VARCHAR(50),
  `TUE` VARCHAR(50),
  `WEN` VARCHAR(50)  ,
  `THU` VARCHAR(50)  ,
  `FRI` VARCHAR(50) , 
   foreign key  morning_activity(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

10.  create moring,afternoon,evening_activity score which is accesible by parent for customisation?
ANS:

/*scores will be given out of 100*/

/*CREATE TABLE `child_care`.`evening_activity_score` (
  `fk_idchild` INT UNSIGNED NOT NULL ,
  `MON` INT(3),
  `TUE` INT(3),
  `WEN` INT(3),
  `THU` INT(3),
  `FRI` INT(3), 
   foreign key  morning_activity(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE `child_care`.`afternoon_activity_score` (
  `fk_idchild` INT UNSIGNED NOT NULL ,
  `MON` INT(3),
  `TUE` INT(3),
  `WEN` INT(3),
  `THU` INT(3),
  `FRI` INT(3), 
   foreign key  morning_activity(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE `child_care`.`morning_activity_score` (
  `fk_idchild` INT UNSIGNED NOT NULL ,
  `MON` INT(3),
  `TUE` INT(3),
  `WEN` INT(3),
  `THU` INT(3),
  `FRI` INT(3), 
   foreign key  morning_activity(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;*/



11. Report table and day_session(morning,afternoon,evening)

ANS:
drop table if exists report;
drop table if exists day_session;

CREATE TABLE child_care.day_session(
idsession INT(2) UNSIGNED NOT NULL UNIQUE,
session_name VARCHAR(40),
 PRIMARY KEY(idsession))
 DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


 /* before creating this table child_info,activity and day_session tables must be created PREVIOUS:PRIMARY KEY(fk_idchild,fk_idactivity); changed to NOW: PRIMARY KEY(fk_idchild,fk_idsession)*/
/* provider id added to this table because we should undderstand who was the provider in that week*/
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

/*archive table is to hold the all weeks reports*/
create table archive like report;

alter table archive
add column dateofdump  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

12. Create AUTHENTICATION TABLE

drop table if exists user_account;
create table user_account(fk_id INT unsigned UNIQUE NOT NULL ,
                          user_name VARCHAR(20) UNIQUE NOT NULL,
                          user_password CHAR(64) UNIQUE NOT NULL,
                          authorisation_key varchar(3),
                          CONSTRAINT fk_id FOREIGN KEY(fk_id)  REFERENCES parent(idparent) ON DELETE CASCADE ON UPDATE CASCADE)
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


------------------------------------------------------------INSERT EXAMPLE---------------------------------------------------------------
1. Insert Into age_group?

insert into age_group(name,min_age,max_age)
value
('Infant',1,2),
('Preschool',2,3),
('kindergarten',3,4);

1.2 Insert Into session?

insert into child_care.day_session
values(1,'Morning Session'),
(2,'Afternoon Session'),
(3,'Evening Session');

2.insert into child_info?

insert into child_info(name,surname,dob,age,fk_age_group)
values('chetan','patitl','1991-02-04',25,(select idage_group from age_group where min_age=1 and max_age=2),
('anil','patitl','1990-02-04',26,(select idage_group from age_group where min_age=1 and max_age=2));
   
3. Insert Into activity?/* you can get more activies for each age group from here:http://www.parents.com/toddlers-preschoolers/activities/indoor/one-year-old-activities/ */
      
	   /*provider id is inserted in activity table one(provider)-many(activities)*/
       insert into activity(activity_name,fk_age_group,fk_session,fk_idcareprovider,activity_description)
       values('FUN WITH TOY',(select idage_group from age_group where min_age=1 and max_age=2),(select idsession from day_session where session_name LIKE 'Morning Session'),1,'A toddler can play withe the bunch of soft,fluffy steralized toys(eg.Bunny,bear,panda)' ),
             ('LITTLE DRUMMERS',(select idage_group from age_group where min_age=1 and max_age=2),(select idsession from day_session where session_name LIKE 'Morning Session'),2,'Find fun tunes to play that have a rousing beat;Skills learned: Coordination, listening skills, and musical exploration' ),
             ('TELEPHONE CALL',(select idage_group from age_group where min_age=1 and max_age=2),(select idsession from day_session where session_name LIKE 'Afternoon Session'),3,'Pretend to make calls, and hold conversations with each other or imaginary people. Use funny voices, and create silly characters on the other line.' ),
             ('LETS COUNT',(select idage_group from age_group where min_age=1 and max_age=2),(select idsession from day_session where session_name LIKE 'Afternoon Session'),4,'Toddlers love to count their fingers and toes."Just like you re giving them new words, numbers are part of life. Use them in context to count toes or objects, so they can eventually learn the concepts of numbers."' ),
             ('PLAYING HOUSE',(select idage_group from age_group where min_age=1 and max_age=2),(select idsession from day_session where session_name LIKE 'Evening Session'),5,'Create a fort out of a cardboard box, play tunnel, or playhouse. Include an entrance and an exit, and encourage your child to go in and out. (You might need to show him at first.) Up the entertainment factor with some pretend play, like knocking on the door or ringing the doorbell, and asking if anyone is home, Dr. Myers suggests.' ),
			 ('TUBE TALK',(select idage_group from age_group where min_age=1 and max_age=2),(select idsession from day_session where session_name LIKE 'Evening Session'),6,'"Kids this age love to play with language, and this activity gives them an opportunity to practice new and novel sounds," Dr. Leiderman says. "' ),

             ('BEACH PARTY',(select idage_group from age_group where min_age=2 and max_age=3),(select idsession from day_session where session_name LIKE 'Morning Session'),1,'Sand and water play are great activities.It gives your child free rein to dig, pour, scoop, and more. Skills learned: Creative play, fine motor skills, tactile stimulation, and social development' ),
			 ('Build-a-Train',(select idage_group from age_group where min_age=2 and max_age=3),(select idsession from day_session where session_name LIKE 'Morning Session'),2,'Perseverance. Games that require team prep work give kids a sense of accomplishment, says clinical psychologist Sandra McLeod Humphrey, author of Hot Issues, Cool Choices: Facing Bullies, Peer Pressure, Popularity, and Put-Downs. The positive payoff introduces children to the good feeling they get when they achieve their hard-earned goals.' ),
             ('The Hot or Cold Game',(select idage_group from age_group where min_age=2 and max_age=3),(select idsession from day_session where session_name LIKE 'Afternoon Session'),3,' Cooperation. This game puts the emphasis on encouraging other players, not competing against them, so preschoolers learn to help each other out in a fun setting.' ),
             ('Counting and Number Recognition',(select idage_group from age_group where min_age=2 and max_age=3),(select idsession from day_session where session_name LIKE 'Afternoon Session'),4,'Since today is the Chinese New Year, we made a dragon-themed set of printables that you can download and print out for your preschooler. In the first activity, they have to count the number of dragons and place the clothes pin on the correct number .' ),
             ('Pouring',(select idage_group from age_group where min_age=2 and max_age=3),(select idsession from day_session where session_name LIKE 'Evening Session'),5,'Some skills require kids to move their wrists. This is called rotary motion. My kids always loved pouring (colored) water from a small pitcher. Other activities that work on this rotary motion might including twisting and taking the lids off jars and tubes, turning door knobs, and assembling nuts and bolts.' ),
             ('Do-a-Dot Fun!',(select idage_group from age_group where min_age=2 and max_age=3),(select idsession from day_session where session_name LIKE 'Evening Session'),6,'We have a letter Dd sheet that the kids can decorate as well. As preschoolers, my kids really liked the do-a-dot (bingo) markers, but they could also use the sheets to paint, color or glue on feathers, pom poms, or foam craft pieces.' ),

				('Tracing lines',(select idage_group from age_group where min_age=3 and max_age=4),(select idsession from day_session where session_name LIKE 'Morning Session'),1,'Tracing Lines Fine Motor Activity - writing practice on a go, write and wipe activity for multiple uses!' ),
				('Make a Square Bubble',(select idage_group from age_group where min_age=3 and max_age=4),(select idsession from day_session where session_name LIKE 'Morning Session'),2,'Sink or float? Kindergarten science experiment Physical Science Standard 1a and focus on water as a liquid' ),
				('Ice Cream Math',(select idage_group from age_group where min_age=3 and max_age=4),(select idsession from day_session where session_name LIKE 'Afternoon Session'),3,'Ice Cream Numbers - preschool summer math that explores fine motor skills, counting, one-to-one correspondence, and more early math skills' ),
				('Color Scavenger',(select idage_group from age_group where min_age=3 and max_age=4),(select idsession from day_session where session_name LIKE 'Afternoon Session'),4,'This simple color scavenger hunt for kids is unbelievably easy to throw together last minute and the kids have fun with it every single year. Great outdoor activity for kids, summer activity for kids, kids camping activity, color learning activity, and preschool color activity.' ),
				('OUT DOOR PLAY',(select idage_group from age_group where min_age=3 and max_age=4),(select idsession from day_session where session_name LIKE 'Evening Session'),5,'Kids will bw taken outdoor in safe environment to play differnt physical games(e.g. running,hide and seek,sea saw,play with different balls)' ),
				('Colors of Nature',(select idage_group from age_group where min_age=3 and max_age=4),(select idsession from day_session where session_name LIKE 'Evening Session'),6,'Kids will be taken out and they will be introduced to diffrent trees,plants and there parts,colors etc.' );
				
--------------------------------------------------------------------------------RETRIVAL_------------------------------------------------------------------
1. Select activity information by providing 'agegroup' and 'day_session time'?
				
select fk_age_group,fk_session,activity_name,activity_description
from activity
where fk_age_group=(select idage_group from age_group where min_age=1 and max_age=2) and fk_session=(select idsession from day_session where session_name LIKE'Afternoon Session')
group by fk_session,fk_age_group,activity_name;
	 
	 or
	 
SET @sessionid=(select idsession from day_session where session_name LIKE 'Afternoon Session');
SET @age_group=(select idage_group from age_group where min_age=1 and max_age=2);
 
select fk_age_group,fk_session,activity_name,activity_description
from activity
where fk_age_group=@age_group and fk_session=@sessionid
group by fk_session,fk_age_group,activity_name;
	 
	 
	
	
2. Get All appropriate Activities available for the child of a parent after parent gets logged in?
	
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
  
  <or>
  select a.idactivity as ActivityId, a.activity_name,ag.name,a.fk_session as SessionID,ds.session_name,a.activity_description
  from activity a join age_group ag join day_session ds
  ON(a.fk_age_group=ag.idage_group and ds.idsession=a.fk_session)
  where fk_age_group=3;     

      /*AS we know parent id taken from login, following query will return the childs of that parent then we can compare those returned chidids with the one parent is inserting to confirm that parent is selecting activity for his child and not for others*/
      /* checking Authorisation */
     SET @curr_parent=1;
     SET @childid=1:  
      select * 
      from child_info 
      where idchild IN(select idchild 
		  from child_info 
		  where fk_idparent=);
       
      Now Selection of activity:- 
       
        SET @childid=2,1....; 
      /*Note******** accurate selection of Activity in a perticualar session is 'very very' important parent must select only those activity which is availabe for that age group in that session*/
	insert into report(fk_idchild,fk_idactivity,fk_idsession)
	values(@childid,13,102),(@childid,16,101),(@childid,17,103);
       
       
       
 3. Admin Will assign the providers to the activities
     a)  first admin will check which are the avilable providers for the activities   
       drop procedure if exists CareProviderChildAssociation1; 
    DELIMITER $$
    create procedure CareProviderChildAssociation1(activityId INT, sessionId INT, providerId INT)
    BEGIN
    UPDATE report
    SET    fk_idprovider=providerId
    WHERE  fk_idactivity=activityId and fk_idsession=sessionId;
    END$$
    DELIMITER ;
    SET @activityid=31;
    SET @sessionid=101;
    SET @providerid=1;
    CALL CareProviderChildAssociation1(31,101,1);
     CALL CareProviderChildAssociation1(32,101,2);
      CALL CareProviderChildAssociation1(33,102,3);
       CALL CareProviderChildAssociation1(34,102,4);
        CALL CareProviderChildAssociation1(35,103,5);
         CALL CareProviderChildAssociation1(36,103,6);
  
       **Now Check Which provider is available in that selected session session**   
       
        SET @sessionid=101,102,....
        select t.*
        from care_provider t
        where t.idcare_provider NOT IN(select fk_idprovider        
				       from report
                                       where fk_idsession=@sessionid and fk_idprovider IS NOT NULL);
				       
				       
http://www.parents.com/toddlers-preschoolers/activities/indoor/one-year-old-activities/

----------------------------------------------------------------------------------------------------------------------------------------
> parent will be registered by admin(parent will enter all information about himself and child)
>then he will enter userid and password 
> admin will enter authorization key   
> parent login with userid and pass.
> first authentication verification will be done.
>if yes, save authentication key, to access throught the session.
> also save child id of parent, to access throught the session.
> now parent can access childinfo,activities appropriate for his/her child along with ids.(from query (2) in retrival section)
> now parent can set activities(looking at above step result) to his child with sessionId,activityId,childId(which is retrived already)and agegroupId for his child.(hehas no rights to add marks)
  SET @childid=(select idchild from parent where idparent=?);/* or take idchild from the saved variable*/
  insert into report(fk_idchild,fk_idactivity,fk_idsession)
  values(1,3,102),(2,1,101);
  
> now admin,provider and parent can sea what are the child activities of the week.
  parent>   want to see the alloted activities.
            <select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name as activityName,ds.session_name as sessionName
			from report r join child_info ci join activity a join day_session ds join age_group ag
			on(r.fk_idactivity=a.idactivity and r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group)
			where r.fk_idchild=2;> 
			
  admin,provider> want to see the report.
            
			<select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name as activityName,ds.session_name as sessionName,r.MON,r.TUE,r.WEN,r.THU,r.FRI
			 from report r join child_info ci join activity a join day_session ds join age_group ag
			 on(r.fk_idactivity=a.idactivity and r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group)
			 where r.fk_idchild=2;>
			
  admin,provider>want to assign the grades
                  ask...which day?,who? id child,activity id?,session id? and what are grades
                  switch(day)
                   {
				     case 1:  Update report
                              SET MON=90
                              where fk_idchild=1 and fk_idactivity=? and fk_idsession=?;
							  break;
					case 2:  Update report
                              SET TUE=90
                              where fk_idchild=1  and fk_idactivity=? and fk_idsession=?;
							  break;
					case 3:  Update report
                              SET WED=90
                              where fk_idchild=1  and fk_idactivity=? and fk_idsession=?;
							  break;
					case 4:  Update report
                              SET THU=90
                              where fk_idchild=1 and fk_idactivity=? and fk_idsession=?;
							  break;
                    case 5:  Update report
                              SET FRI=90
                              where fk_idchild=1  and fk_idactivity=? and fk_idsession=?;
							  break;							  
					default:  System.out.println("Invalid");		  
                   }				  

/* get result of a perticual student for the perticular session*/
select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,ds.session_name as sessionName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total, ((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag
on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group)
where r.fk_idchild=1 and r.fk_idsession=101
group by ci.idchild;

/*get result of a student group by all sessions*/
select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,ds.session_name as sessionName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total, ((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag
on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group)
where r.fk_idchild=1 
group by ci.idchild,ds.session_name;


/* get result of a student according to his/her activities*/ 
select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name  as activityName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total,((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag join activity a
on(r.fk_idchild=ci.idchild and r.fk_idactivity=a.idactivity and ci.fk_age_group=ag.idage_group)
where r.fk_idchild=1 
group by a.activity_name,ci.idchild;

select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,ds.session_name as sessionName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total, ((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag
on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group)
group by ci.idchild,ds.session_name;

select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,ds.session_name as sessionName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total, ((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag
on(r.fk_idchild=ci.idchild and r.fk_idsession=ds.idsession and ci.fk_age_group=ag.idage_group)
group by ci.idchild;

/*all students in all activitis top tanking*/
select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name  as activityName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total,((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag join activity a
on(r.fk_idchild=ci.idchild and r.fk_idactivity=a.idactivity and ci.fk_age_group=ag.idage_group)
group by a.activity_name,ci.idchild
order by Percentage desc;

/* top ranking students in a perticular activity*/
select ci.idchild,ci.name,ci.surname,ci.dob, ag.name as ageGroup,a.activity_name  as activityName,(ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0)) as total,((ifnull(r.MON,0)+ifnull(r.TUE,0)+ifnull(r.WEN,0)+ifnull(r.THU,0)+ifnull(r.FRI,0))*100/500) as Percentage
from report r join child_info ci join day_session ds join age_group ag join activity a
on(r.fk_idchild=ci.idchild and r.fk_idactivity=a.idactivity and ci.fk_age_group=ag.idage_group)
where r.fk_idactivity=2
group by a.activity_name,ci.idchild
order by Percentage desc;				   

/*Roleby:anyone; get activity,session,provider,rating; by providing agegroup*/

 select a.idactivity as ActivityId,a.activity_name as ActivityName,ag.name as AgeGroup,ds.session_name as SessionName,cp.name as ProviderName,r.rate,a.activity_description
 from activity a JOIN age_group ag JOIN day_session ds JOIN care_provider cp JOIN rating r
 on(a.fk_age_group=ag.idage_group and a.fk_session=ds.idsession and a.fk_idcareprovider=cp.idcare_provider and r.fk_idprovider=a.fk_idcareprovider)
 where a.fk_age_group=2;
 
/*Roleby:anyone; get activity,session,provider,rating; Overall*/ 
 select a.idactivity as ActivityId,a.activity_name as ActivityName,ag.name as AgeGroup,ds.session_name as SessionName,cp.name as ProviderName,r.rate,a.activity_description
 from activity a JOIN age_group ag JOIN day_session ds JOIN care_provider cp JOIN rating r
 on(a.fk_age_group=ag.idage_group and a.fk_session=ds.idsession and a.fk_idcareprovider=cp.idcare_provider and r.fk_idprovider=a.fk_idcareprovider)

 --------------------------------------------------------------------------------------------------------------------------------------------
 /*if currentday is saturday......then.....
 1.dump to archive.
 2.send email.
 3.cleanup.
 
 ZoneId z = ZoneId.of( "America/Montreal" );
LocalDate today = LocalDate.now( z );
DayOfWeek dow = today.getDayOfWeek();*/
