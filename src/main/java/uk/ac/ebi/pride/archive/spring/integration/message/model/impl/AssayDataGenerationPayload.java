package uk.ac.ebi.pride.archive.spring.integration.message.model.impl;

import lombok.*;

/** @author Suresh Hewapathirana */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AssayDataGenerationPayload {
  @NonNull private String accession;
  @NonNull private String assayAccession;
}
