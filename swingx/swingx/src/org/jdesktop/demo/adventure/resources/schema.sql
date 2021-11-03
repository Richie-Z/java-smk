-- File containing schema for demo database

create table category (
    catid char(20) not null,
    locale varchar(10) not null,
    name varchar(80) null,
    description varchar(255) null,
    imageuri varchar(80) null,
    constraint pk_category primary key (catid, locale)
)
;

create table lodging (
    lodgingid char(20) not null,
    locale varchar(10) not null,
    location varchar(30) not null,
    name varchar(80) not null,
    description varchar(255) not null,    
    price decimal(10,2) null,
    imageuri varchar(80) not null,   
    constraint pk_lodging primary key (lodgingid , locale )
)
;

create table package (
    packageid char(20) not null,
    catid char(20) not null,
    locale varchar(10) not null,
    location varchar(30) not null,
    price decimal(10,2) not null,
    name varchar(80) null,
    description varchar(255) null,
    imageuri varchar(80) not null,
    lodgingid char(20) not null,
    constraint pk_package primary key (packageid, locale) ,
    constraint fk_package_1 foreign key (catid, locale)
        references category (catid, locale),
    constraint fk_package_2 foreign key (lodgingid, locale)
        references lodging (lodgingid, locale)
)
;

create table activity (
    activityid char(20) not null,
    locale varchar(10) not null,
    location varchar(30) not null,
    name varchar(80) not null,
    description varchar(255) not null,    
    price decimal(10,2) null,
    imageuri varchar(80) not null,   
    constraint pk_activity primary key (activityid , locale )
)
;

create table activitylist (
    packageid char(20) not null,
    activityid char(20) not null,
    locale varchar(10) not null,
    constraint pk_activitylist primary key (packageid , activityid ,locale),
    constraint fk_activitylist_1 foreign key (packageid , locale)
        references package (packageid, locale),
    constraint fk_activitylist_2 foreign key (activityid , locale)
        references activity(activityid , locale)
)
;

create table transportation (
    transportationid char(20) not null,
    locale varchar(10) not null,
    origin varchar(30) not null,
    destination varchar(30) not null,
    carrier varchar(80) not null,
    name varchar(80) not null,
    departuretime varchar(80) not null, 
    arrivaltime varchar(80) not null,
    description varchar(255) not null,  
    class varchar(20) not null,
    price decimal(10,2) null,
    imageuri varchar(80) not null,   
    constraint pk_transportation primary key (transportationid , locale )
)
;

create table signon (
    username varchar(25) not null,
    password varchar(25)  not null,
    constraint pk_signon primary key (username)
)
;

create table account (
    userid varchar(80) not null,
    email varchar(80) not null,
    firstname varchar(80) not null,
    lastname varchar(80) not null,
    addr1 varchar(80) not null,
    addr2 varchar(40) null,
    city varchar(80) not  null,
    state varchar(80) not null,
    zip varchar(20) not null,
    country varchar(20) not null,
    phone varchar(80) not null,
    constraint pk_account primary key (userid)
)
;

INSERT INTO category VALUES ('ISLAND', 'en_US', 'Island Adventures', 'Experience an island paradise in a way fit for your needs.','http://azonic.sfbay.sun.com/adventure/images/Island_Adventures.gif')
;
INSERT INTO category VALUES ('JUNGLE', 'en_US', 'Jungle Adventures', 'Experience a jungle paradise in a way fit for your needs.','http://azonic.sfbay.sun.com/adventure/images/Jungle_Adventures.gif')
;
INSERT INTO category VALUES ('MOUNTAIN', 'en_US', 'Mountain Adventures', 'Experience an elevated paradise with a view.','http://azonic.sfbay.sun.com/adventure/images/Mountain_Adventures.gif')
;
INSERT INTO category VALUES ('ORBITAL', 'en_US', 'Orbital Adventures', 'Experience a vacuum paradise with a beautiful view and where no one can hear you scream.','http://azonic.sfbay.sun.com/adventure/images/Space_Adventures.gif')
;
INSERT INTO category VALUES ('WESTERN', 'en_US', 'Western Adventures', 'Enjoy the Wild West.','http://azonic.sfbay.sun.com/adventure/images/Western_Adventures.gif')
;
INSERT INTO category VALUES ('SOUTH_POLE', 'en_US', 'South Pole Adventures', 'Experience a frozen paradise in a way fit for your needs.','http://azonic.sfbay.sun.com/adventure/images/SouthPole_Adventures.gif')
;


INSERT INTO lodging VALUES ('LODG-1', 'en_US', 'The Island of Maui', 'Budget Hotel','per night single ocuppancy' ,265.00,'http://azonic.sfbay.sun.com/adventure/images/Budget_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-2', 'en_US', 'The Islands of the Bahamas', 'Downtown Luxury Hotel', 'per night single ocuppancy' ,260.00,'http://azonic.sfbay.sun.com/adventure/images/Luxury_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-3', 'en_US', 'The Islands of Tahiti', 'Budget Hotel','per night single ocuppancy' ,187.00,'http://azonic.sfbay.sun.com/adventure/images/Budget_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-4', 'en_US', 'Amazon Jungle', 'Jungle Cave Hotel','per night single ocuppancy' ,255.00,'http://azonic.sfbay.sun.com/adventure/images/Cave_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-5', 'en_US', 'Amazon Jungle', 'Jungle Tent Hotel','per night single ocuppancy' ,156.00,'http://azonic.sfbay.sun.com/adventure/images/Tent_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-6', 'en_US', 'Mt.Kilimanjaro', 'Mountain Cabin Hotel','per night single ocuppancy' ,195.00,'http://azonic.sfbay.sun.com/adventure/images/Log_Cabin_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-7', 'en_US', 'Mt.Kilimanjaro', 'Mountain Cave Hotel','per night single ocuppancy' ,187.00,'http://azonic.sfbay.sun.com/adventure/images/Cave_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-8', 'en_US', 'Space', 'Space Hotel','per night single ocuppancy' ,49887.00,'http://azonic.sfbay.sun.com/adventure/images/Space_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-9', 'en_US', 'Antarctica', 'Snow Cave Hotel','per night single ocuppancy' ,289.00,'http://azonic.sfbay.sun.com/adventure/images/Snow_Cave_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-10', 'en_US', 'Antarctica', 'Icy Igloo' ,'per night single ocuppancy' ,267.00,'http://azonic.sfbay.sun.com/adventure/images/Igloo_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-11', 'en_US', 'Texas', 'Old Western Hotel' ,'per night single ocuppancy' ,227.00,'http://azonic.sfbay.sun.com/adventure/images/Budget_Hotel_Icon.gif')
;
INSERT INTO lodging VALUES ('LODG-12', 'en_US', 'Texas', 'Budget Hotel' ,'per night single ocuppancy' ,189.00,'http://azonic.sfbay.sun.com/adventure/images/Budget_Hotel_Icon.gif')
;

INSERT INTO  package VALUES ('PACK-1','ISLAND', 'en_US', 'The Island of Maui', 1115.00,'Maui Survival Adventure', 'Practice your survival skills in an island paradise.', 'http://azonic.sfbay.sun.com/adventure/images/Island_Survival.gif','LODG-1')
;
INSERT INTO  package VALUES ('PACK-2','ISLAND', 'en_US', 'The Islands of the Bahamas', 1135.00,'Bahamas Relaxation trip', 'Relax, unwind and enjoy in an island paradise.', 'http://azonic.sfbay.sun.com/adventure/images/Island_Relax.gif','LODG-2')
;
INSERT INTO  package VALUES ('PACK-3','ISLAND', 'en_US', 'The Islands of Tahiti', 1269.00,'Tahiti Snorkeling Adventure', 'Practice your snorkeling skills in an island paradise.', 'http://azonic.sfbay.sun.com/adventure/images/Island_Snorkeling.gif','LODG-3')
;
INSERT INTO  package VALUES ('PACK-4','JUNGLE','en_US', 'Amazon Jungle', 1756.00,'Amazon Appreciation Adventure','Practice your appreciation skills in a jungle paradise.','http://azonic.sfbay.sun.com/adventure/images/Jungle_Appreciation.gif','LODG-4')
;
INSERT INTO  package VALUES ('PACK-5','JUNGLE','en_US', 'Amazon Jungle', 1213.00,'Amazon Survival Adventure','Explore the amazon rainforest.','http://azonic.sfbay.sun.com/adventure/images/Jungle_Survival.gif','LODG-5')
;
INSERT INTO  package VALUES ('PACK-6','MOUNTAIN','en_US', 'Mt.Kilimanjaro', 1495.00,'Mountain Climbing Adventure','Practice your climing skills in an elevated paradise.','http://azonic.sfbay.sun.com/adventure/images/Mountain_Climbing.gif','LODG-6')
;
INSERT INTO  package VALUES ('PACK-7','MOUNTAIN','en_US', 'Mt.Kilimanjaro', 1676.00,'Mountain Relaxation Adventure','Practice your relaxation skills in a elevated paradise.','http://azonic.sfbay.sun.com/adventure/images/Mountain_Relax.gif','LODG-7')
;
INSERT INTO  package VALUES ('PACK-8','ORBITAL','en_US', 'Space', 1999999.00,'Orbital Appreciation Adventure', 'See the earth from a new perspective.', 'http://azonic.sfbay.sun.com/adventure/images/Space_Appreciation.gif','LODG-8')
;
INSERT INTO  package VALUES ('PACK-9','SOUTH_POLE','en_US', 'Antarctica', 19957.00,'South Pole Survival Adventure', 'Practice your survival skills in a frozen paradise.', 'http://azonic.sfbay.sun.com/adventure/images/SouthPole_Survival.gif','LODG-9')
;
INSERT INTO  package VALUES ('PACK-10','SOUTH_POLE','en_US', 'Antarctica', 18764.00,'South Pole Relaxation Adventure', 'Practice your relaxation skills in a frozen paradise.', 'http://azonic.sfbay.sun.com/adventure/images/SouthPole_Relax.gif','LODG-10')
;
INSERT INTO  package VALUES ('PACK-11','WESTERN' ,'en_US', 'Texas', 764.00,'Urban Cowboy Adventure', 'Practice your cowboy skills in the wild wild west.', 'http://azonic.sfbay.sun.com/adventure/images/Western_Urban_Cowboy.gif','LODG-11')
;
INSERT INTO  package VALUES ('PACK-12','WESTERN', 'en_US', 'Texas', 1862.00,'Dude Ranch Adventure', 'Practice your cowboy skills in a western paradise.', 'http://azonic.sfbay.sun.com/adventure/images/Western_Dude_Ranch.gif','LODG-12')
;

INSERT INTO activity VALUES ('ACTY-1', 'en_US', 'The Island of Maui', 'Snorkeling','Learn about life under the sea',100.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Boat_Ride_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-2', 'en_US', 'The Island of Maui','Helicopter Ride' , 'Get a spectacular view of the island',125.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Helicopter_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-3', 'en_US', 'The Island of Maui','Surfing', 'Experience the thrill - ride the waves',139.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Surfing_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-4', 'en_US', 'The Islands of the Bahamas', 'Snorkeling','Snorkel in crystal clear waters and learn  about life under the sea',300.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Boat_Ride_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-5', 'en_US', 'The Islands of the Bahamas','Spa Masssge' , 'Be pampered and relax ',325.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Spa_Massage_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-6', 'en_US', 'The Islands of the Bahamas', 'Fishing','Different species of fishes - the fish lover will never get disappointed',285.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Fishing_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-7', 'en_US', 'The Islands of Tahiti', 'Snorkeling','Snorkel in crystal clear waters and learn about life under the sea ',300.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Snorkeling_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-8', 'en_US', 'The Islands of Tahiti','Para Sailing' , 'Enjoy a great ride with a view',325.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_ParaSail_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-9', 'en_US', 'The Islands of Tahiti', 'Fishing','Different species of fishes - the fish lover will never get disappointed',455.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Boat_Ride_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-10', 'en_US', 'Amazon Jungle', 'Boat Ride', 'Get a spectacular view of exotic flora and fauna',200.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Boat_Ride_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-11', 'en_US', 'Amazon Jungle', 'Hiking', 'Explore the jungle',350.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Hiking_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-12', 'en_US', 'Amazon Jungle', 'Bird Watching', 'For the bird lover . View hundreds of different species of birds',199.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Bird_Watching_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-13', 'en_US', 'Amazon Jungle', 'Rafting', 'Enjoy rafting in the amazon',145.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Rafting_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-14', 'en_US', 'Amazon Jungle', 'Crocodile Wrestling', 'Make new reptile friends while practicing your half nelson',189.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Crocodile_Wrestling_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-15', 'en_US', 'Mt.Kilimanjaro', 'Mountain Climbing', 'Enjoy the view from the summit',129.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Mountain_Climb_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-16', 'en_US', 'Mt.Kilimanjaro', 'Mountain Biking', 'Bike to a hidden waterfall',189.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Mountain_Bike_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-17', 'en_US', 'Mt.Kilimanjaro', 'Hiking', 'Explore the mountain',146.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Hiking_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-18', 'en_US', 'Mt.Kilimanjaro', 'Bird Watching', 'For the bird lover . View hundreds of different species of birds',172.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Bird_Watching_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-19', 'en_US', 'Mt.Kilimanjaro', 'Spa Masssge' , 'Be pampered and relax',172.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Spa_Massage_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-20', 'en_US', 'Space', 'Space Walk', 'Get the best view of the earth possible',1899867.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Space_Walk_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-21', 'en_US', 'Space', 'Space Experiments', 'Find answers to all your questions about space',1893477.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Space_Experiment_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-22', 'en_US', 'Antarctica', 'Boat Ride', 'Get a spectacular view from at sea on a luxurious yacht',367.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Boat_Ride_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-23', 'en_US', 'Antarctica', 'Helicopter Ride', 'A memorable experience in the sky',389.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Helicopter_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-24', 'en_US', 'Antarctica', 'Hiking', 'Explore the South Pole',307.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Hiking_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-25', 'en_US', 'Antarctica', 'Barbeque', 'For the food lover',320.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_BBQ_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-26', 'en_US', 'Antarctica', 'Fishing', 'Different species of fishes - the fish lover will never get disappointed',530.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Boat_Ride_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-27', 'en_US', 'Texas', 'Barbeque', 'Enjoy the finest cuisine the wild wild west has to offer',350.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_BBQ_Icon.gif')
;
INSERT INTO activity VALUES ('ACTY-28', 'en_US', 'Texas' , 'Bull Ride', 'Great way to reduce stress',320.00,'http://azonic.sfbay.sun.com/adventure/images/Activity_Bull_Ride_Icon.gif')
;

INSERT INTO activitylist VALUES ('PACK-1', 'ACTY-1', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-1', 'ACTY-2', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-1', 'ACTY-3', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-2', 'ACTY-4', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-2', 'ACTY-5', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-2', 'ACTY-6', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-3', 'ACTY-7', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-3', 'ACTY-8', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-3', 'ACTY-9', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-4', 'ACTY-10', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-4', 'ACTY-11', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-4', 'ACTY-12', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-5', 'ACTY-13', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-5', 'ACTY-11', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-5', 'ACTY-14', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-6', 'ACTY-15', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-6', 'ACTY-16', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-6', 'ACTY-17', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-7', 'ACTY-17', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-7', 'ACTY-18', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-7', 'ACTY-19', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-8', 'ACTY-20', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-8', 'ACTY-21', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-9', 'ACTY-22', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-9', 'ACTY-23', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-9', 'ACTY-24', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-10', 'ACTY-25', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-10', 'ACTY-26', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-11', 'ACTY-27', 'en_US')
;
INSERT INTO activitylist VALUES ('PACK-12', 'ACTY-28', 'en_US')
;

INSERT INTO transportation VALUES ('TRPN-1','en_US', 'Los Angeles' ,'The Island of Maui', 'Island Airlines', 'IA1234','10:30 AM','4:30 PM' , 'Airbus A319','economy class',600.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-2','en_US', 'The Island of Maui', 'Los Angeles' , 'Island Airlines', 'IA1694','6:00 PM','1:00 AM' , 'Airbus A320','business class',1000.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-3','en_US', 'Orlando' ,'The Islands of the Bahamas' , 'Dragon Airlines', 'DA1468','6:00 PM','11:55 PM' , 'Airbus A319','economy class',1750.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-4', 'en_US', 'The Islands of the Bahamas' , 'Orlando', 'Dragon Airlines', 'DA4567','11:00 AM','4:55 PM', 'Airbus A319','business class',2100.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-5', 'en_US', 'Austin', 'The Islands of Tahiti' , 'Island Airlines', 'IA4389','11:00 AM','6:00 PM', 'Airbus A320','economy class',2500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-6', 'en_US', 'The Islands of Tahiti' , 'Austin' , 'Island Airlines', 'IA8319','10:30 PM','5:30 AM', 'Airbus A320','economy class',2500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-7', 'en_US', 'Columbus' , 'Amazon Jungle' , 'Jungle King Airlines', 'JK4832','11:00 AM','8:00 PM', 'Airbus A319','business class',2100.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-8', 'en_US', 'Amazon Jungle' , 'Columbus' , 'Jungle King Airlines', 'JK9231','9:00 PM','6:00 AM', 'Airbus A319','economy class',2900.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-9', 'en_US', 'Detroit' , 'Mt.Kilimanjaro' , 'Mountain Airlines', 'MA5012','11:00 AM','10:00 PM', 'Airbus A320','economy class',2100.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-10', 'en_US', 'Mt.Kilimanjaro' , 'Detroit' , 'Mountain Airlines', 'MA6821','10:30 PM','9:30 AM', 'Airbus A319','business class',2225.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-11', 'en_US', 'Los Angeles' , 'Texas', 'Wild West Airlines', 'WA6932','11:00 AM','2:00 PM', 'Airbus A319','business class',1485.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-12', 'en_US', 'Texas', 'Los Angeles' , 'Wild West Airlines', 'WA4483','5:30 PM','8:30 PM', 'Airbus A320','economy class',500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-13','en_US', 'Austin' ,'The Island of Maui', 'Island Airlines', 'IA1689','11:30 AM','5:30 PM' , 'Airbus A319','economy class',600.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-14','en_US', 'The Island of Maui', 'Austin' , 'Island Airlines', 'IA1527','7:00 PM','2:00 AM' , 'Airbus A320','business class',1000.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-15','en_US', 'Los Angeles' ,'The Islands of the Bahamas' , 'Dragon Airlines', 'DA1398','5:00 PM','10:55 PM' , '  Boeing A319','economy class',1750.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-16', 'en_US', 'The Islands of the Bahamas' , 'Los Angeles', 'Dragon Airlines', 'DA9376','10:00 AM','3:55 PM', 'Boeing A319','business class',2100.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-17', 'en_US', 'Columbus' , 'The Islands of Tahiti' , 'Island Airlines', 'IA5678','10:00 AM','5:00 PM', 'Boeing A320','economy class',2500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-18', 'en_US', 'The Islands of Tahiti' , 'Columbus' , 'Island Airlines', 'IA8754','9:30 PM','4:30 AM', 'Airbus A320','economy class',2500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-19', 'en_US', 'Orlando' , 'Amazon Jungle' , 'Jungle King Airlines', 'JK5678','10:00 AM','7:00 PM', 'Airbus A319','business class',2100.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-20', 'en_US', 'Amazon Jungle' , 'Orlando' , 'Jungle King Airlines', 'JK7991','8:00 PM','5:00 AM', 'Boeing A319','economy class',2900.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-21', 'en_US', 'Los Angeles' , 'Mt.Kilimanjaro' , 'Mountain Airlines', 'MA9542','10:00 AM','9:00 PM', 'Airbus A320','economy class',2100.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-22', 'en_US', 'Mt.Kilimanjaro' , 'Los Angeles' , 'Mountain Airlines', 'MA9024','9:30 PM','8:30 AM', 'Airbus A319','business class',2225.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-23', 'en_US', 'Detroit' , 'Texas', 'Wild West Airlines', 'WA7893','10:00 AM','3:00 PM', 'Airbus A319','business class',1485.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-24', 'en_US', 'Texas', 'Detroit' , 'Wild West Airlines', 'WA9432','5:30 PM','10:30 PM', 'Airbus A320','economy class',500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-25', 'en_US', 'Space' , 'Florida', 'Space Airlines', 'WA7401','11:00 AM','3:00 PM', 'Space Shuttle','business class',21000550.00,'http://azonic.sfbay.sun.com/adventure/images/Shuttle_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-26', 'en_US', 'Florida', 'Space' , 'Space Airlines', 'WA4701','7:30 PM','10:30 PM', 'Space Shuttle','business class',21000550.00,'http://azonic.sfbay.sun.com/adventure/images/Shuttle_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-27', 'en_US', 'Detroit' , 'Antarctica ', 'South Pole Airlines', 'WA7801','10:00 AM','3:00 PM', 'Boeing A319','business class',4500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;
INSERT INTO transportation VALUES ('TRPN-28', 'en_US', 'Antarctica ', 'Detroit' , 'South Pole Airlines', 'WA4901','5:30 PM','10:30 PM', 'Boeing A320','economy class',4500.00,'http://azonic.sfbay.sun.com/adventure/images/Airplane_Icon.gif')
;

INSERT INTO signon VALUES('j2ee','j2ee')
;
INSERT INTO account VALUES('j2ee', 'yourname@yourdomain.com','ABC', 'XYZ', '123 Some Road','Apartment 206', 'Santa Clara', 'CA', '94303', 'USA', '555-555-5555')
;
