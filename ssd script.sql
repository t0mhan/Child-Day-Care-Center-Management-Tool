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

2. create child info table ?
ANS: 

CREATE TABLE `child_care`.`child_info` (
  `idchild` INT(3) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `age` INT UNSIGNED NOT NULL,
  `fk_age_group` INT UNSIGNED NULL,
  PRIMARY KEY (`child_id`),
  FOREIGN KEY child_info(fk_age_group) references  age_group(idage_group),
  UNIQUE INDEX `indx_child_id` (`child_id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;;

3. create parent table?
ANS:

CREATE TABLE `child_care`.`parent` (
  `idparent` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `fk_idchild` INT UNSIGNED NOT NULL ,
  UNIQUE INDEX `indx_idparent` (`idparent` ASC),
  PRIMARY KEY (`idparent`),
  FOREIGN KEY parent(`fk_idchild`) REFERENCES  child_info(idchild) ON DELETE CASCADE) 
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

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

5. create food table?

CREATE TABLE `child_care`.`food` (
  `day` CHAR(3) NULL,
  `breakfast` VARCHAR(50) NULL,
  `lunch` VARCHAR(50) NULL,
  `snak` VARCHAR(50) NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


6.   create activity,material table?
    ANS:
	
	  CREATE TABLE `child_care`.`activity` (
	  `idactivity` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	  `activity_name` VARCHAR(45) NOT NULL,
	  PRIMARY KEY (`idactivity`),
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
	
7.  create careprovider
    ANS:

    CREATE TABLE `child_care`.`care_provider` (
  `idcare_provider` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `emailid` VARCHAR(50) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  `rating` DECIMAL(2,1) NULL,
  `fk_idactivity` INT UNSIGNED, 
  PRIMARY KEY (`idcare_provider`),
  FOREIGN KEY(fk_idactivity) REFERENCES  activity(idactivity))
   ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;
	
   ALTER TABLE `child_care`.`care_provider` 
   ADD UNIQUE INDEX `indx_idcareprovider` (`idcare_provider` ASC);	
   
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

insert into child_care.day_session
values(1,'Morning Session'),
(2,'Afternoon Session'),
(3,'Evening Session');
 
CREATE TABLE `child_care`.`report` (
  fk_idchild INT unsigned NOT NULL ,
  fk_idactivity INT unsigned NOT NULL,
  fk_idsession  INT unsigned  NOT NULL,
  `MON` INT(3) NULL,
  `TUE` INT(3) NULL,
  `WEN` INT(3) NULL,
  `THU` INT(3) NULL,
  `FRI` INT(3) NULL,
  CONSTRAINT report.fk_idsession FOREIGN KEY(fk_idsession)  REFERENCES child_care.day_session(idsession),
  FOREIGN KEY report(fk_idchild) REFERENCES child_info(idchild) ON DELETE CASCADE,
  FOREIGN KEY report(fk_idactivity) REFERENCES activity(idactivity) ON DELETE CASCADE,
  PRIMARY KEY(fk_idchild,fk_idactivity)
  )ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;










	 
	 
	 
	
	
	
	





