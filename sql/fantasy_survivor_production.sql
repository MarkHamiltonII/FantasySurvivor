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
    `name` varchar(1024) not null
);

create table season_castaway (
	season_id int not null,
	castaway_id int not null,
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
    constraint fk_league_season_id
		foreign key (season_id)
        references season(season_id),
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
    constraint fk_league_id
		foreign key (league_id)
        references league(league_id),
	constraint fk_user_id
		foreign key (user_id)
        references app_user(user_id)
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
    tribal_points int not null,
    points_to_date int not null,
    lau_id int not null,
    tribal_id int not null,
    constraint fk_laut_lau_id 
		foreign key (lau_id)
        references league_app_user(id),
	constraint fk_laut_id
		foreign key (tribal_id)
        references tribal(tribal_id)
);

insert into app_user (username, password_hash, disabled) values
	('diablo','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('mork','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('joj','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('kennedy','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('shaggy','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('jt','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0),
    ('lil lady','$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa',0);

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
    (7,1);

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
    ('Zach', 'Wurtenberger', 22, 'St. Louis, MO', 'Student', 'https://wwwimage-tve.cbsstatic.com/thumbnails/photos/w425-q80/cast/cbb_18_800x1000.jpg', 'https://www.cbs.com/shows/survivor/cast/216624/');
    
insert into season(season_id, `name`) values
	(42, 'Season 42');
    
insert into season_castaway values
	(42,1),
    (42,2),
    (42,3),
    (42,4),
    (42,5),
    (42,6),
    (42,7),
    (42,8),
    (42,9),
    (42,10),
    (42,11),
    (42,12),
    (42,13),
    (42,14),
    (42,15),
    (42,16),
    (42,17),
    (42,18);
    
insert into league(`name`, season_id) values
	('Our first league', 42);
    
insert into league_app_user(league_id,user_id) values
	(1,4);
    
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
    (1,42,18);
    
insert into league_app_user_tribal(tribal_points, points_to_date, lau_id, tribal_id) values
	(100,100,1,1),
	(93,193,1,2);