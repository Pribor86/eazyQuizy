CREATE TABLE player (
    name varchar(30) NOT NULL UNIQUE,
    correct int,
    wrong int
);

CREATE TABLE topic (
	id serial primary key,
	topic varchar(30)
);

CREATE TABLE question (
	id serial primary key,
	question text,
	difficulty char(1),
	topic_id int references topic(id)
);

CREATE TABLE answer (
    id serial primary key,
	answer text,
	isCorrect boolean,
	question_id int references question(id)
);