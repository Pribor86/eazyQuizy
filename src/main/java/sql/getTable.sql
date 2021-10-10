select t.topic, q.question, q.difficulty, a.answer, a.iscorrect
from topic t, question q, answer a
where t.id = q.topic_id
and q.id = a.question_id;