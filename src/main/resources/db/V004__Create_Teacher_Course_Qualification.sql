CREATE TABLE IF NOT EXISTS teacher_course_qualification (
    teacher_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,

    PRIMARY KEY (teacher_id, course_id),

    CONSTRAINT fk_qualification_teacher
        FOREIGN KEY (teacher_id)
        REFERENCES teachers(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_qualification_course
        FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_teacher_qualification ON teacher_course_qualification(teacher_id);
CREATE INDEX idx_course_qualification ON teacher_course_qualification(course_id);

COMMENT ON TABLE teacher_course_qualification IS 'Join table representing which teachers are qualified to teach which courses (ManyToMany)';
COMMENT ON COLUMN teacher_course_qualification.teacher_id IS 'Reference to the teacher';
COMMENT ON COLUMN teacher_course_qualification.course_id IS 'Reference to the course the teacher is qualified for';
