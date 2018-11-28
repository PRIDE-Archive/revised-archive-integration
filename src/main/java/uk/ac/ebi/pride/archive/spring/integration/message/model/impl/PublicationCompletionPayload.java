package uk.ac.ebi.pride.archive.spring.integration.message.model.impl;

import lombok.*;
import uk.ac.ebi.pride.archive.spring.integration.message.model.ProjectAccessionPayload;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PublicationCompletionPayload implements ProjectAccessionPayload,Serializable {
    @NonNull
    private String accession;
}
