package com.backbase.oss.boat.bay;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.backbase.oss.boat.bay");

        noClasses()
            .that()
            .resideInAnyPackage("com.backbase.oss.boat.bay.service..")
            .or()
            .resideInAnyPackage("com.backbase.oss.boat.bay.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.backbase.oss.boat.bay.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
