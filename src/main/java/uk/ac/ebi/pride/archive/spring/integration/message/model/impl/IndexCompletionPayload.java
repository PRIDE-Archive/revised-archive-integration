package uk.ac.ebi.pride.archive.spring.integration.message.model.impl;

import lombok.*;
import uk.ac.ebi.pride.archive.spring.integration.message.model.IndexType;
import uk.ac.ebi.pride.archive.spring.integration.message.model.IndexTypePayload;
import uk.ac.ebi.pride.archive.spring.integration.message.model.ProjectAccessionPayload;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class IndexCompletionPayload implements ProjectAccessionPayload, IndexTypePayload, Serializable {
    @NonNull
    private String accession;
    @NonNull
    private IndexType indexType;
}
