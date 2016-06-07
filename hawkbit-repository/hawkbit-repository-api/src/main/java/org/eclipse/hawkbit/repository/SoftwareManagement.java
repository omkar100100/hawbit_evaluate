/**
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.hawkbit.repository;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.eclipse.hawkbit.im.authentication.SpPermission.SpringEvalExpressions;
import org.eclipse.hawkbit.repository.exception.EntityAlreadyExistsException;
import org.eclipse.hawkbit.repository.exception.EntityNotFoundException;
import org.eclipse.hawkbit.repository.exception.RSQLParameterSyntaxException;
import org.eclipse.hawkbit.repository.exception.RSQLParameterUnsupportedFieldException;
import org.eclipse.hawkbit.repository.model.AssignedSoftwareModule;
import org.eclipse.hawkbit.repository.model.DistributionSet;
import org.eclipse.hawkbit.repository.model.SoftwareModule;
import org.eclipse.hawkbit.repository.model.SoftwareModuleMetadata;
import org.eclipse.hawkbit.repository.model.SoftwareModuleType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Service for managing {@link SoftwareModule}s.
 *
 */
public interface SoftwareManagement {

    /**
     * Counts {@link SoftwareModule}s with given
     * {@link SoftwareModule#getName()} or {@link SoftwareModule#getVersion()}
     * and {@link SoftwareModule#getType()} that are not marked as deleted.
     *
     * @param searchText
     *            to search for in name and version
     * @param type
     *            to filter the result
     * @return number of found {@link SoftwareModule}s
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Long countSoftwareModuleByFilters(String searchText, SoftwareModuleType type);

    /**
     * Count all {@link SoftwareModule}s in the repository that are not marked
     * as deleted.
     *
     * @return number of {@link SoftwareModule}s
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Long countSoftwareModulesAll();

    /**
     * Counts {@link SoftwareModule}s with given {@link SoftwareModuleType}.
     *
     * @param type
     *            to count
     * @return number of found {@link SoftwareModule}s
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Long countSoftwareModulesByType(@NotNull SoftwareModuleType type);

    /**
     * @return number of {@link SoftwareModuleType}s in the repository.
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Long countSoftwareModuleTypesAll();

    /**
     * Create {@link SoftwareModule}s in the repository.
     *
     * @param swModules
     *            {@link SoftwareModule}s to create
     * @return SoftwareModule
     * @throws EntityAlreadyExistsException
     *             if a given entity already exists
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_CREATE_REPOSITORY)
    List<SoftwareModule> createSoftwareModule(@NotNull Collection<SoftwareModule> swModules);

    /**
     *
     * @param swModule
     *            SoftwareModule to create
     * @return SoftwareModule
     * @throws EntityAlreadyExistsException
     *             if a given entity already exists
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_CREATE_REPOSITORY)
    SoftwareModule createSoftwareModule(@NotNull SoftwareModule swModule);

    /**
     * creates a list of software module meta data entries.
     *
     * @param metadata
     *            the meta data entries to create or update
     * @return the updated or created software module meta data entries
     * @throws EntityAlreadyExistsException
     *             in case one of the meta data entry already exists for the
     *             specific key
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_UPDATE_REPOSITORY)
    List<SoftwareModuleMetadata> createSoftwareModuleMetadata(@NotNull Collection<SoftwareModuleMetadata> metadata);

    /**
     * creates or updates a single software module meta data entry.
     *
     * @param metadata
     *            the meta data entry to create or update
     * @return the updated or created software module meta data entry
     * @throws EntityAlreadyExistsException
     *             in case the meta data entry already exists for the specific
     *             key
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_UPDATE_REPOSITORY)
    SoftwareModuleMetadata createSoftwareModuleMetadata(@NotNull SoftwareModuleMetadata metadata);

    /**
     * Creates multiple {@link SoftwareModuleType}s.
     *
     * @param types
     *            to create
     * @return created {@link Entity}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_CREATE_REPOSITORY)
    List<SoftwareModuleType> createSoftwareModuleType(@NotNull final Collection<SoftwareModuleType> types);

    /**
     * Creates new {@link SoftwareModuleType}.
     *
     * @param type
     *            to create
     * @return created {@link Entity}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_CREATE_REPOSITORY)
    SoftwareModuleType createSoftwareModuleType(@NotNull SoftwareModuleType type);

    /**
     * Deletes the given {@link SoftwareModule} {@link Entity}.
     *
     * @param bsm
     *            is the {@link SoftwareModule} to be deleted
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_DELETE_REPOSITORY)
    void deleteSoftwareModule(@NotNull SoftwareModule bsm);

    /**
     * deletes a software module meta data entry.
     *
     * @param id
     *            the ID of the software module meta data to delete
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_UPDATE_REPOSITORY)
    void deleteSoftwareModuleMetadata(@NotNull SoftwareModule softwareModule, @NotNull String key);

    /**
     * Deletes {@link SoftwareModule}s which is any if the given ids.
     *
     * @param ids
     *            of the Software Modules to be deleted
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_DELETE_REPOSITORY)
    void deleteSoftwareModules(@NotNull Collection<Long> ids);

    /**
     * Deletes or marks as delete in case the type is in use.
     *
     * @param type
     *            to delete
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_DELETE_REPOSITORY)
    void deleteSoftwareModuleType(@NotNull SoftwareModuleType type);

    /**
     * @param pageable
     *            the page request to page the result set
     * @param set
     *            to search for
     * @return all {@link SoftwareModule}s that are assigned to given
     *         {@link DistributionSet}.
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModule> findSoftwareModuleByAssignedTo(@NotNull Pageable pageable, @NotNull DistributionSet set);

    /**
     * @param pageable
     *            the page request to page the result set
     * @param set
     *            to search for
     * @param type
     *            to filter
     * @return all {@link SoftwareModule}s that are assigned to given
     *         {@link DistributionSet} filtered by {@link SoftwareModuleType}.
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModule> findSoftwareModuleByAssignedToAndType(@NotNull Pageable pageable, @NotNull DistributionSet set,
            @NotNull SoftwareModuleType type);

    /**
     * Filter {@link SoftwareModule}s with given
     * {@link SoftwareModule#getName()} or {@link SoftwareModule#getVersion()}
     * and {@link SoftwareModule#getType()} that are not marked as deleted.
     *
     * @param pageable
     *            page parameter
     * @param searchText
     *            to be filtered as "like" on {@link SoftwareModule#getName()}
     * @param type
     *            to be filtered as "like" on {@link SoftwareModule#getType()}
     * @return the page of found {@link SoftwareModule}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Slice<SoftwareModule> findSoftwareModuleByFilters(@NotNull Pageable pageable, String searchText,
            SoftwareModuleType type);

    /**
     * Finds {@link SoftwareModule} by given id.
     *
     * @param id
     *            to search for
     * @return the found {@link SoftwareModule}s or <code>null</code> if not
     *         found.
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY + SpringEvalExpressions.HAS_AUTH_OR
            + SpringEvalExpressions.IS_CONTROLLER)
    SoftwareModule findSoftwareModuleById(@NotNull Long id);

    /**
     * retrieves {@link SoftwareModule} by their name AND version AND type..
     *
     * @param name
     *            of the {@link SoftwareModule}
     * @param version
     *            of the {@link SoftwareModule}
     * @param type
     *            of the {@link SoftwareModule}
     * @return the found {@link SoftwareModule} or <code>null</code>
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    SoftwareModule findSoftwareModuleByNameAndVersion(@NotEmpty String name, @NotEmpty String version,
            @NotNull SoftwareModuleType type);

    /**
     * finds a single software module meta data by its id.
     *
     * @param id
     *            the id of the software module meta data containing the meta
     *            data key and the ID of the software module
     * @return the found SoftwareModuleMetadata or {@code null} if not exits
     * @throws EntityNotFoundException
     *             in case the meta data does not exists for the given key
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    SoftwareModuleMetadata findSoftwareModuleMetadata(@NotNull SoftwareModule softwareModule, @NotEmpty String key);

    /**
     * finds all meta data by the given software module id.
     *
     * @param swId
     *            the software module id to retrieve the meta data from
     * @param pageable
     *            the page request to page the result
     * @return a paged result of all meta data entries for a given software
     *         module id
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModuleMetadata> findSoftwareModuleMetadataBySoftwareModuleId(@NotNull Long swId,
            @NotNull Pageable pageable);

    /**
     * finds all meta data by the given software module id.
     *
     * @param softwareModuleId
     *            the software module id to retrieve the meta data from
     * @param spec
     *            the specification to filter the result
     * @param pageable
     *            the page request to page the result
     * @return a paged result of all meta data entries for a given software
     *         module id
     * 
     * @throws RSQLParameterUnsupportedFieldException
     *             if a field in the RSQL string is used but not provided by the
     *             given {@code fieldNameProvider}
     * @throws RSQLParameterSyntaxException
     *             if the RSQL syntax is wrong
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModuleMetadata> findSoftwareModuleMetadataBySoftwareModuleId(@NotNull Long softwareModuleId,
            @NotNull String rsqlParam, @NotNull Pageable pageable);

    /**
     * Filter {@link SoftwareModule}s with given
     * {@link SoftwareModule#getName()} or {@link SoftwareModule#getVersion()}
     * search text and {@link SoftwareModule#getType()} that are not marked as
     * deleted and sort them by means of given distribution set related modules
     * on top of the list.
     * 
     * After that the modules are sorted by {@link SoftwareModule#getName()} and
     * {@link SoftwareModule#getVersion()} in ascending order.
     *
     * @param pageable
     *            page parameter
     * @param orderByDistributionId
     *            the ID of distribution set to be ordered on top
     * @param searchText
     *            filtered as "like" on {@link SoftwareModule#getName()}
     * @param type
     *            filtered as "equal" on {@link SoftwareModule#getType()}
     * @return the page of found {@link SoftwareModule}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Slice<AssignedSoftwareModule> findSoftwareModuleOrderBySetAssignmentAndModuleNameAscModuleVersionAsc(
            @NotNull Pageable pageable, @NotNull Long orderByDistributionId, String searchText,
            SoftwareModuleType type);

    /**
     * Retrieves all software modules. Deleted ones are filtered.
     *
     * @param pageable
     *            pagination parameter
     * @return the found {@link SoftwareModule}s
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Slice<SoftwareModule> findSoftwareModulesAll(@NotNull Pageable pageable);

    /**
     * Retrieves all software modules with a given list of ids
     * {@link SoftwareModule#getId()}.
     *
     * @param ids
     *            to search for
     * @return {@link List} of found {@link SoftwareModule}s
     */
    List<SoftwareModule> findSoftwareModulesById(@NotEmpty Collection<Long> ids);

    /**
     * Retrieves all {@link SoftwareModule}s with a given specification.
     *
     * @param spec
     *            the specification to filter the software modules
     * @param pageable
     *            pagination parameter
     * @return the found {@link SoftwareModule}s
     * 
     * @throws RSQLParameterUnsupportedFieldException
     *             if a field in the RSQL string is used but not provided by the
     *             given {@code fieldNameProvider}
     * @throws RSQLParameterSyntaxException
     *             if the RSQL syntax is wrong
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModule> findSoftwareModulesByPredicate(@NotNull String rsqlParam, @NotNull Pageable pageable);

    /**
     * retrieves the {@link SoftwareModule}s by their {@link SoftwareModuleType}
     * .
     *
     * @param pageable
     *            page parameters
     * @param type
     *            to be filtered on
     * @return the found {@link SoftwareModule}s
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Slice<SoftwareModule> findSoftwareModulesByType(@NotNull Pageable pageable, @NotNull SoftwareModuleType type);

    /**
     *
     * @param id
     *            to search for
     * @return {@link SoftwareModuleType} in the repository with given
     *         {@link SoftwareModuleType#getId()}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    SoftwareModuleType findSoftwareModuleTypeById(@NotNull Long id);

    /**
     *
     * @param key
     *            to search for
     * @return {@link SoftwareModuleType} in the repository with given
     *         {@link SoftwareModuleType#getKey()}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    SoftwareModuleType findSoftwareModuleTypeByKey(@NotNull String key);

    /**
     *
     * @param name
     *            to search for
     * @return all {@link SoftwareModuleType}s in the repository with given
     *         {@link SoftwareModuleType#getName()}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    SoftwareModuleType findSoftwareModuleTypeByName(@NotNull String name);

    /**
     * @param pageable
     *            parameter
     * @return all {@link SoftwareModuleType}s in the repository.
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModuleType> findSoftwareModuleTypesAll(@NotNull Pageable pageable);

    /**
     * Retrieves all {@link SoftwareModuleType}s with a given specification.
     *
     * @param spec
     *            the specification to filter the software modules types
     * @param pageable
     *            pagination parameter
     * @return the found {@link SoftwareModuleType}s
     * 
     * @throws RSQLParameterUnsupportedFieldException
     *             if a field in the RSQL string is used but not provided by the
     *             given {@code fieldNameProvider}
     * @throws RSQLParameterSyntaxException
     *             if the RSQL syntax is wrong
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    Page<SoftwareModuleType> findSoftwareModuleTypesAll(@NotNull String rsqlParam, @NotNull Pageable pageable);

    /**
     * Retrieves software module including details (
     * {@link SoftwareModule#getArtifacts()}).
     *
     * @param id
     *            parameter
     * @param isDeleted
     *            parameter
     * @return the found {@link SoftwareModule}s
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_READ_REPOSITORY)
    SoftwareModule findSoftwareModuleWithDetails(@NotNull Long id);

    /**
     * Updates existing {@link SoftwareModule}. Update-able values are
     * {@link SoftwareModule#getDescription()}
     * {@link SoftwareModule#getVendor()}.
     *
     * @param sm
     *            to update
     *
     * @return the saved {@link Entity}.
     *
     * @throws NullPointerException
     *             of {@link SoftwareModule#getId()} is <code>null</code>
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_UPDATE_REPOSITORY)
    SoftwareModule updateSoftwareModule(@NotNull SoftwareModule sm);

    /**
     * updates a distribution set meta data value if corresponding entry exists.
     *
     * @param metadata
     *            the meta data entry to be updated
     * @return the updated meta data entry
     * @throws EntityNotFoundException
     *             in case the meta data entry does not exists and cannot be
     *             updated
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_UPDATE_REPOSITORY)
    SoftwareModuleMetadata updateSoftwareModuleMetadata(@NotNull SoftwareModuleMetadata metadata);

    /**
     * Updates existing {@link SoftwareModuleType}. Update-able value is
     * {@link SoftwareModuleType#getDescription()} and
     * {@link SoftwareModuleType#getColour()}.
     *
     * @param sm
     *            to update
     * @return updated {@link Entity}
     */
    @PreAuthorize(SpringEvalExpressions.HAS_AUTH_UPDATE_REPOSITORY)
    SoftwareModuleType updateSoftwareModuleType(@NotNull SoftwareModuleType sm);

}
