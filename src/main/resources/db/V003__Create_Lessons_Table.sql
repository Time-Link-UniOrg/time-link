CREATE TABLE IF NOT EXISTS lessons (
    id SERIAL PRIMARY KEY,
    lesson_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    original_teacher_id INTEGER NOT NULL,
    actual_teacher_id INTEGER,
    group_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    notes VARCHAR(500),
    status VARCHAR(30) DEFAULT 'SCHEDULED',

    CONSTRAINT fk_lesson_original_teacher
        FOREIGN KEY (original_teacher_id)
        REFERENCES teachers(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson_actual_teacher
        FOREIGN KEY (actual_teacher_id)
        REFERENCES teachers(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_lesson_group
        FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_lesson_course
        FOREIGN KEY (course_id)
        REFERENCES courses(id)
        ON DELETE CASCADE,

    CONSTRAINT check_lesson_times
        CHECK (end_time > start_time),

    CONSTRAINT check_lesson_status
        CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'SUBSTITUTE_PENDING', 'SUBSTITUTE_CONFIRMED'))
);

CREATE INDEX idx_lesson_date ON lessons(lesson_date);
CREATE INDEX idx_lesson_original_teacher ON lessons(original_teacher_id);
CREATE INDEX idx_lesson_actual_teacher ON lessons(actual_teacher_id);
CREATE INDEX idx_lesson_group ON lessons(group_id);
CREATE INDEX idx_lesson_course ON lessons(course_id);
CREATE INDEX idx_lesson_status ON lessons(status);

CREATE INDEX idx_lesson_teacher_date ON lessons(original_teacher_id, lesson_date);
CREATE INDEX idx_lesson_actual_teacher_date ON lessons(actual_teacher_id, lesson_date);

COMMENT ON TABLE lessons IS 'Stores scheduled lessons with teacher, group, and course information';
COMMENT ON COLUMN lessons.original_teacher_id IS 'Teacher originally assigned to teach this lesson';
COMMENT ON COLUMN lessons.actual_teacher_id IS 'Teacher actually teaching (for substitutions, NULL if no substitute)';
COMMENT ON COLUMN lessons.status IS 'Current status of the lesson';