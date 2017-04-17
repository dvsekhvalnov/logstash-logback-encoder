/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.logstash.logback.composite.loggingevent;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.fieldnames.LogstashFieldNames;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;

public class UuidProviderTest
{
    public static final String UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private UuidProvider provider = new UuidProvider();

    @Mock
    private JsonGenerator generator;

    @Mock
    private ILoggingEvent event;

    @Test
    public void testDefaultName() throws IOException
    {
        provider.writeTo(generator, event);

        verify(generator).writeStringField(eq(UuidProvider.FIELD_UUID), matches(UUID));
    }

    @Test
    public void testFieldName() throws IOException
    {
        provider.setFieldName("newFieldName");

        provider.writeTo(generator, event);

        verify(generator).writeStringField(eq("newFieldName"), matches(UUID));
    }

    @Test
    public void testStrategy() throws IOException
    {
        provider.setStrategy("time");

        provider.writeTo(generator, event);

        verify(generator).writeStringField(eq("uuid"), matches(UUID));
    }

    @Test
    public void testEthernet() throws IOException
    {
        provider.setStrategy("time");
        provider.setEthernet("00:C0:F0:3D:5B:7C");

        provider.writeTo(generator, event);

        verify(generator).writeStringField(eq("uuid"), matches(UUID));
    }
}