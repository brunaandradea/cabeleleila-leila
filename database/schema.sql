-- Script de criacao do banco de dados salao_db
-- Ordem respeita as dependencias de chave estrangeira:
-- usuario e servico nao dependem de ninguem, agendamento depende de usuario,
-- item_agendamento depende de agendamento e servico.

CREATE DATABASE IF NOT EXISTS salao_db;
USE salao_db;

CREATE TABLE usuario (
    id CHAR(36) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(60) NOT NULL,
    telefone VARCHAR(50) NOT NULL,
    perfil VARCHAR(20) NOT NULL
);

CREATE TABLE servico (
    id CHAR(36) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco_atual DECIMAL(10,2) NOT NULL,
    duracao_minutos INT NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE agendamento (
    id CHAR(36) PRIMARY KEY,
    usuario_id CHAR(36) NOT NULL,
    data_hora DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    observacao VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE item_agendamento (
    id CHAR(36) PRIMARY KEY,
    agendamento_id CHAR(36) NOT NULL,
    servico_id CHAR(36) NOT NULL,
    preco_praticado DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id) ON DELETE CASCADE,
    FOREIGN KEY (servico_id) REFERENCES servico(id)
);
