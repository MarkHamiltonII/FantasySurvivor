drop database if exists fantasy_survivor;
create database fantasy_survivor;
use fantasy_survivor;

create table app_user (
	user_id int primary key auto_increment,
    username varchar(125) not null unique,
    password_hash varchar(2048) not null,
    disabled bit not null default(0)
);

create table app_roles (
	role_id int primary key auto_increment,
    `role` varchar(25) not null
);

create table app_user_roles (
	user_id int not null,
	role_id int not null,
    constraint pk_app_user_role
        primary key (user_id, role_id),
	constraint fk_app_user_roles_user_id
		foreign key (user_id)
        references app_user(user_id),
	constraint fk_app_user_roles_role_id
		foreign key (role_id)
        references app_roles(role_id) 
);

create table castaway (
	castaway_id int primary key auto_increment,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    age int null,
    current_residence varchar(250) null,
    occupation varchar(100) null,
    icon_url varchar(2048) not null,
    page_url varchar(2048) not null
);

create table season (
	season_id int primary key,
    `name` varchar(256) not null,
    unique (`name`)
);

create table season_castaway (
	season_id int not null,
	castaway_id int not null,
    tribe varchar(25) null,
    tribe_color varchar(25) null,
    constraint pk_season_castaway
        primary key (season_id, castaway_id),
	constraint fk_season_castaway_season_id
		foreign key (season_id)
        references season(season_id),
	constraint fk_season_castaway_castaway_id
		foreign key (castaway_id)
        references castaway(castaway_id) 
);

create table league (
	league_id int primary key auto_increment,
    `name` varchar(256) not null,
    season_id int not null,
    owner_id int not null,
    constraint fk_league_season_id
		foreign key (season_id)
        references season(season_id),
	constraint fk_league_owner
		foreign key (owner_id)
        references app_user(user_id),
	unique key league_uniq_key (season_id, `name`)
);

create table tribal (
	tribal_id int primary key auto_increment,
	tribal_number int,
    season_id int not null,
    castaway_id int not null,
    constraint fk_tribal_season_id
		foreign key (season_id)
        references season(season_id),
    constraint fk_tribal_castaway_id
		foreign key (castaway_id)
        references castaway(castaway_id)
);

create table league_app_user (
	id int primary key auto_increment,
    league_id int not null,
    user_id int not null,
    final bit not null default(0),
    constraint fk_league_id
		foreign key (league_id)
        references league(league_id),
	constraint fk_user_id
		foreign key (user_id)
        references app_user(user_id),
	unique key lau_uniq_key (league_id, user_id)
);

create table league_app_user_rating (
	laur_id int primary key auto_increment,
    rating int not null,
    castaway_id int not null,
	lau_id int not null,
    constraint fk_laur_castaway_id
		foreign key (castaway_id)
        references castaway(castaway_id),
	constraint fk_laur_lau_id
		foreign key (lau_id)
        references league_app_user(id),
	unique key uniq_key (lau_id, rating)
);

create table league_app_user_tribal (
	laut_id int primary key auto_increment,
    tribal_points decimal(5,2) not null,
    points_to_date decimal(7,2) not null,
    lau_id int not null,
    tribal_number int not null,
    constraint fk_laut_lau_id 
		foreign key (lau_id)
        references league_app_user(id),
	unique key laut_uniq_key (lau_id, tribal_number)
);

insert into app_user (username, password_hash, disabled) values
	('david','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('mark','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('jojo','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('kennedy','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('shaggy','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('jt','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('amanda','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('pearl','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('anna','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('ashley','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('brittany','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('christina','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('derek','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('erik','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('jacob','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('jenna','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('katey','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('kyle','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('dawson','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('matthew','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('mfs','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('mike','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('meagan','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('sam','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('tena','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('vincent','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('casey','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('madison','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('kenny','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('dan','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0);

insert into app_roles (`role`) values 
	('USER'),
    ('LEAGUE_OWNER'),
    ('ADMIN');
    
insert into app_user_roles values
	(1,3),
    (1,2),
    (1,1),
    (2,1),
    (2,2),
    (2,3),
    (3,1),
    (4,1),
    (5,1),
    (6,1),
    (7,1),
    (8,1),
    (9,1),
    (10,1),
    (11,1),
    (12,1),
    (13,1),
    (14,1),
    (15,1),
    (16,1),
    (17,1),
    (18,1),
    (19,1),
    (20,1),
    (21,1),
    (22,1),
    (23,1),
    (24,1),
    (25,1),
    (26,1),
    (27,1),
    (28,1),
    (29,1),
    (30,1);

insert into castaway (first_name, last_name, age, current_residence, occupation, icon_url, page_url) values
	('Chanelle', 'Howell', 29, 'Hamden, CT', 'Executive Recruiter', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_02_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216607/'),
    ('Daniel', 'Strunk', 30, 'New Haven, CT', 'Law Clerk', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_04_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216608/'),
    ('Drea', 'Wheeler', 35, 'Montreal, Quebec', 'Fitness Consultant', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_13_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216609/'),
    ('Hai', 'Giang', 29, 'New Orleans, LA', 'Data Scientist', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_05_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216610/'),
    ('Jackson', 'Fox', 48, 'Houston, TX', 'Healthcare Worker', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_10_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216611/'),
    ('Jenny', 'Kim', 43, 'Brooklyn, NY', 'Creative Director', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_01_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216612/'),
    ('Jonathan', 'Young', 29, 'Gulfshores, AL', 'Beach Service Co. Owner', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_12_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216613/'),
    ('Lindsay', 'Dolashewich', 31, 'Asbury Park, NJ', 'Dietitian', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_08_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216614/'),
    ('Lydia', 'Meredith', 22, 'Santa Monica, CA', 'Waitress', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_03_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216615/'),
    ('Marya', 'Sherron', 47, 'Noblesville, IN', 'Stay at Home Mom', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_07_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216616/'),
    ('Maryanne', 'Oketch', 24, 'Ajax, Ontario', 'Seminary Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_09_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216617/'),
    ('Mike', 'Turner', 58, 'Hoboken, NJ', 'Retired Firefighter', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_06_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216618/'),
    ('Omar', 'Zaheer', 31, 'Whitby, Ontario', 'Veterinarian', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_11_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216619/'),
    ('Rocksroy', 'Bailey', 44, 'Las Vegas, NV', 'Stay at Home Dad', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_16_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216620/'),
    ('Romeo', 'Escobar', 37, 'Norwalk, CA', 'Pageant Coach', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_17_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216621/'),
    ('Swati', 'Goel', 19, 'Palo Alto, Ca', 'Ivy League Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_15_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216622/'),
    ('Tori', 'Meehan', 25, 'Tulsa, OK', 'Therapist', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_14_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216623/'),
    ('Zach', 'Wurtenberger', 22, 'St. Louis, MO', 'Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_18_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216624/'),
    ('Brandon', 'Cottom', 30, 'Newton, PA', 'Security Specialist','https://static.wikia.nocookie.net/survivor/images/9/95/S44_brandon_t.png','https://survivor.fandom.com/wiki/Brandon_Cottom'),
    ('Bruce', 'Perreault', 46, 'Warwick, RI', 'Insurance Agent', 'https://static.wikia.nocookie.net/survivor/images/c/c3/S44_bruce_t.png', 'https://survivor.fandom.com/wiki/Bruce_Perreault'),
    ('Carolyn', 'Wiger', 35, 'North St. Paul, MN', 'Drug Councelor', 'https://static.wikia.nocookie.net/survivor/images/7/7d/S44_carolyn_t.png', 'https://survivor.fandom.com/wiki/Carolyn_Wiger'),
    ('Caston', 'Garrett', 20, 'Atlanta, GA', 'NASA Engineering Student', 'https://static.wikia.nocookie.net/survivor/images/4/42/S44_carson_t.png', 'https://survivor.fandom.com/wiki/Carson_Garrett'),
    ('Claire', 'Rafson', 25, 'Brooklyn, NY', 'Tech Investor', 'https://static.wikia.nocookie.net/survivor/images/9/91/S44_claire_t.png', 'https://survivor.fandom.com/wiki/Claire_Rafson'),
    ('Danny', 'Massa', 32, 'Bronx, NY', 'NYC Firefighter', 'https://static.wikia.nocookie.net/survivor/images/9/92/S44_danny_t.png', 'https://survivor.fandom.com/wiki/Danny_Massa'),
    ('Frannie', 'Marin', 23, 'St. Paul, MN', 'Research Coordinator', 'https://static.wikia.nocookie.net/survivor/images/3/33/S44_frannie_t.png', 'https://survivor.fandom.com/wiki/Frannie_Marin'),
    ('Heidi', 'Lagares-Greenblatt', 43, 'Pittsburgh, PA', 'Engineering Manager', 'https://static.wikia.nocookie.net/survivor/images/1/13/S44_heidi_t.png', 'https://survivor.fandom.com/wiki/Heidi_Lagares-Greenblatt'),
    ('Helen', 'Li', 29, 'San Fransisco, CA', 'Product Manager', 'https://static.wikia.nocookie.net/survivor/images/7/7d/S44_helen_t.png', 'https://survivor.fandom.com/wiki/Helen_Li'),
    ('Jamie Lynn', 'Ruiz', 35, 'Mesa, AZ', 'Yogi', 'https://static.wikia.nocookie.net/survivor/images/d/da/S44_jaime_t.png', 'https://survivor.fandom.com/wiki/Jaime_Lynn_Ruiz'),
    ('Josh', 'Wilder', 34, 'Cincinnati, OH', 'Surgical Podiatrist', 'https://static.wikia.nocookie.net/survivor/images/9/92/S44_josh_t.png', 'https://survivor.fandom.com/wiki/Josh_Wilder'),
    ('Kane', 'Fritzler', 25, 'Moose Jaw, Canada', 'Law Student', 'https://static.wikia.nocookie.net/survivor/images/3/3b/S44_kane_t.png', 'https://survivor.fandom.com/wiki/Kane_Fritzler'),
    ('Lauren', 'Harpe', 31, 'Mont Belvieu, TX', 'Elementary School Teacher', 'https://static.wikia.nocookie.net/survivor/images/4/4e/S44_lauren_t.png', 'https://survivor.fandom.com/wiki/Lauren_Harpe'),
    ('Maddie', 'Pomilla', 28, 'Brooklyn, NY', 'Charity Projects Manager', 'https://static.wikia.nocookie.net/survivor/images/c/c7/S44_maddy_t.png', 'https://survivor.fandom.com/wiki/Maddy_Pomilla'),
    ('Matt', 'Blankinship', 27, 'San Francisco, CA', 'Security Software Engineer', 'https://static.wikia.nocookie.net/survivor/images/e/ed/S44_matt_t.png', 'https://survivor.fandom.com/wiki/Matt_Blankinship'),
    ('Matthew', 'Grinstead-Mayle', 43, 'Columbus, OH', 'Barbershop Owner', 'https://static.wikia.nocookie.net/survivor/images/b/be/S44_matthew_t.png', 'https://survivor.fandom.com/wiki/Matthew_Grinstead-Mayle'),
    ('Sarah', 'Wade', 27, 'Chicago, IL', 'Management Consultant', 'https://static.wikia.nocookie.net/survivor/images/7/78/S44_sarah_t.png', 'https://survivor.fandom.com/wiki/Sarah_Wade'),
    ('Yam Yam', 'Arocho', 26, 'San Juan, PR', 'Salon Owner', 'https://static.wikia.nocookie.net/survivor/images/3/38/S44_yam_yam_t.png', 'https://survivor.fandom.com/wiki/Yam_Yam_Arocho');
    
insert into season(season_id, `name`) values
	(42, 'Season 42'),
    (44, 'Season 44');
    
insert into season_castaway values
	(42,1,'Vati','Green'),
    (42,2,'Vati','Green'),
    (42,3,'Ika','Blue'),
    (42,4,'Vati','Green'),
    (42,5,'Taku','Orange'),
    (42,6,'Vati','Green'),
    (42,7,'Taku','Orange'),
    (42,8,'Taku','Orange'),
    (42,9,'Vati','Green'),
    (42,10,'Taku','Orange'),
    (42,11,'Taku','Orange'),
    (42,12,'Vati','Green'),
    (42,13,'Taku','Orange'),
    (42,14,'Ika','Blue'),
    (42,15,'Ika','Blue'),
    (42,16,'Ika','Blue'),
    (42,17,'Ika','Blue'),
    (42,18,'Ika','Blue'),
    (44,19,'Ratu','Orange'),
    (44,20,'Tika','Purple'),
    (44,21,'Tika','Purple'),
    (44,22,'Tika','Purple'),
    (44,23,'Soka','Green'),
    (44,24,'Soka','Green'),
    (44,25,'Soka','Green'),
    (44,26,'Soka','Green'),
    (44,27,'Tika','Purple'),
    (44,28,'Ratu','Orange'),
    (44,29,'Soka','Green'),
    (44,30,'Ratu','Orange'),
    (44,31,'Ratu','Orange'),
    (44,32,'Ratu','Orange'),
    (44,33,'Soka','Green'),
    (44,34,'Ratu','Orange'),
    (44,35,'Tika','Purple'),
    (44,36,'Tika','Purple');
    
insert into league(`name`, season_id, owner_id) values
	('Our first league', 42, 1),
    ('Fantasy Survivor: 44', 44, 1);
    
insert into league_app_user(league_id,user_id) values
	(1,4),
    (1,3),
    (1,2),
    (1,1),
    (2,1),
    (2,2),
    (2,3),
    (2,4),
    (2,5),
    (2,6),
    (2,7),
    (2,8),
    (2,9),
    (2,10),
    (2,11),
    (2,12),
    (2,13),
    (2,14),
    (2,15),
    (2,16),
    (2,17),
    (2,18),
    (2,19),
    (2,20),
    (2,21),
    (2,22),
    (2,23),
    (2,24),
    (2,25),
    (2,26),
    (2,27),
    (2,28),
    (2,29),
    (2,30);
    
    -- kennedy's list 
insert into league_app_user_rating(rating,castaway_id,lau_id) values
	(11,1,1),
    (14,2,1),
    (6,3,1),
    (7,4,1),
    (10,6,1),
    (12,7,1),
    (15,8,1),
    (9,9,1),
    (16,10,1),
    (4,11,1),
    (5,12,1),
    (3,13,1),
    (2,14,1),
    (1,15,1),
    (13,16,1),
    (8,17,1);
    
insert into tribal(tribal_number,season_id, castaway_id) values
	(1,42,1),
    (1,42,2),
    (1,42,3),
    (1,42,4),
    (1,42,6),
    (1,42,7),
    (1,42,8),
    (1,42,9),
    (1,42,11),
    (1,42,12),
    (1,42,13),
    (1,42,14),
    (1,42,15),
    (1,42,16),
    (1,42,17),
    (2,42,1),
    (2,42,2),
    (2,42,3),
    (2,42,4),
    (2,42,7),
    (2,42,8),
    (2,42,9),
    (2,42,11),
    (2,42,12),
    (2,42,13),
    (2,42,14),
    (2,42,15),
    (2,42,16),
    (2,42,17);
    
insert into league_app_user_tribal(tribal_points, points_to_date, lau_id, tribal_number) values
	(100,100,1,1),
	(93,193,1,2);