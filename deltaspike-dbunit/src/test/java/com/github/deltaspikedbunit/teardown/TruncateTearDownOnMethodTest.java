/*
 * Copyright 2002-2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.deltaspikedbunit.teardown;

import com.github.deltaspikedbunit.annotation.DatabaseOperation;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.deltaspike.DatabaseNoRollbackTest;
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryCategory;
import com.github.deltaspikedbunit.sample.inventory.repository.InventoryCategoryRepository;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;


@RunWith(CdiTestRunner.class)
@DatabaseNoRollbackTest
public class TruncateTearDownOnMethodTest {

    @Inject
    private InventoryCategoryRepository inventoryRepository;

    @Test
    @DatabaseTearDown(value = "../truncate-db.xml", type = DatabaseOperation.TRUNCATE_TABLE)
    public void test() throws Exception {
        List<InventoryCategory> categories = inventoryRepository.findAll();
        Assert.assertEquals(1, categories.size());
        Assert.assertEquals("existing_category", categories.get(0).getCategoryName());
    }

    @After
    public void afterTest() throws Exception {
        InventoryCategory category = inventoryRepository.findByName("existing_category");
        Assert.assertEquals(1000, category.getId());
        Assert.assertEquals(0, category.getItems().size());
    }

}
