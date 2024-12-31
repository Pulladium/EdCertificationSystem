package com.vozh.art.dto;


import com.vozh.art.entity.SignedDoc;

public record SignedDocAndFile(SignedDoc doc, byte[] file) {
}
