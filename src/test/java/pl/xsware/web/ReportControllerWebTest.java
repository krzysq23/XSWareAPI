package pl.xsware.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.xsware.TestMockConfig;
import pl.xsware.api.ReportController;
import pl.xsware.domain.model.report.ReportFile;
import pl.xsware.domain.service.ReportService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReportController.class)
@Import(TestMockConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReportControllerWebTest {

    @Autowired
    private ReportService reportService;
    @Autowired
    private MockMvc mvc;

    @Test
    void testGeneratePdf() throws Exception {

        Mockito.when(reportService.generateFileReport(any(), eq("pdf")))
                .thenReturn(
                        ReportFile.builder().filename("test.pdf").mediaType(MediaType.APPLICATION_PDF).file(new byte[]{1,2,3}).build()
                );

        mvc.perform(post("/api/reports/generate/pdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dateStart\":\"2025-01-01\",\"dateEnd\":\"2025-01-31\"}"))
                .andExpect(status().isOk());
    }
}
