CREATE TABLE IF NOT EXISTS student_parent (
    student_id INTEGER NOT NULL,
    parent_id INTEGER NOT NULL,

    PRIMARY KEY (student_id, parent_id),

    CONSTRAINT fk_student_parent_student
        FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_student_parent_parent
        FOREIGN KEY (parent_id)
        REFERENCES parents(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_student_parents ON student_parent(student_id);
CREATE INDEX idx_parent_children ON student_parent(parent_id);

COMMENT ON TABLE student_parent IS 'Join table representing the relationship between students and their parents (ManyToMany)';
COMMENT ON COLUMN student_parent.student_id IS 'Reference to the student';
COMMENT ON COLUMN student_parent.parent_id IS 'Reference to the parent/guardian';

