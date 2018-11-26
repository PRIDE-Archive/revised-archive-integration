package uk.ac.ebi.pride.archive.spring.integration.message.model.impl;

import lombok.*;
import uk.ac.ebi.pride.archive.spring.integration.message.model.FileType;
import uk.ac.ebi.pride.archive.spring.integration.message.model.FileTypePayload;
import uk.ac.ebi.pride.archive.spring.integration.message.model.ProjectAccessionPayload;


import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FileGenerationPayload implements ProjectAccessionPayload, FileTypePayload, Serializable {
    @NonNull
    private String accession;
    @NonNull
    private FileType fileType;
}
