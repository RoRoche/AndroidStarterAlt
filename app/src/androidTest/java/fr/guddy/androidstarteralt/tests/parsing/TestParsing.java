package fr.guddy.androidstarteralt.tests.parsing;

import android.support.test.InstrumentationRegistry;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.polok.localify.LocalifyClient;

import org.frutilla.FrutillaTestRunner;
import org.frutilla.annotations.Frutilla;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import fr.guddy.androidstarteralt.rest.dto.DTORepo;
import fr.guddy.androidstarteralt.test.R;

import static com.google.common.truth.Truth.assertThat;

@RunWith(FrutillaTestRunner.class)
public class TestParsing {

    //region Fields
    private LocalifyClient mLocalifyClient;
    //endregion

    //region Test lifecycle
    @Before
    public void setUp() throws Exception {
        mLocalifyClient = new LocalifyClient.Builder()
                .withResources(InstrumentationRegistry.getContext().getResources())
                .build();
    }
    //endregion

    //region Test methods
    @Frutilla(
            Given = "A single GitHub repo from a JSON file",
            When = "Parsing this content with LoganSquare",
            Then = "It should have parsed a repo named \"git-consortium\""
    )
    @Test
    public void test_Parse_JSONRepo_ParsesRepoDTO() throws IOException {
        String lsRepoData;
        Given:
        {
            lsRepoData = mLocalifyClient.localify().loadRawFile(R.raw.repo_octocat);
        }

        DTORepo loRepoDTO;
        When:
        {
            loRepoDTO = LoganSquare.parse(lsRepoData, DTORepo.class);
        }

        Then:
        {
            assertThat(loRepoDTO).isNotNull();
            assertThat(loRepoDTO.name).isEqualTo("git-consortium");
        }
    }

    @Frutilla(
            Given = "Multiple GitHub repos from a JSON file",
            When = "Parsing this content with LoganSquare",
            Then = "It should have parsed a repo named \"git-consortium\""
    )
    @Test
    public void test_Parse_JSONArrayRepo_ParsesRepoAsArrayDTO() throws IOException {
        String lsRepoDataAsArray;
        Given:
        {
            lsRepoDataAsArray = mLocalifyClient.localify().loadRawFile(R.raw.repos_octocat);
        }

        List<DTORepo> lloRepoAsArrayDTO;
        When:
        {
            lloRepoAsArrayDTO = LoganSquare.parseList(lsRepoDataAsArray, DTORepo.class);
        }

        Then:
        {
            assertThat(lloRepoAsArrayDTO).hasSize(1);
            assertThat(lloRepoAsArrayDTO.get(0).name).isEqualTo("git-consortium");
        }
    }
    //endregion
}
