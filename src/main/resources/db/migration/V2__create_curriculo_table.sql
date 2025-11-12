CREATE TABLE curriculos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cargo_atual VARCHAR(255),
    cargo_desejado VARCHAR(255),
    habilidades TEXT,
    experiencia TEXT,
    educacao TEXT,
    pdf_url VARCHAR(255),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_curriculo_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_curriculos_usuario_id ON curriculos(usuario_id);
