package com.embarkx.companyms.company.messaging;

import com.embarkx.companyms.company.CompanyService;
import com.embarkx.companyms.company.dto.ReviewMessage;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Data
@Service
public class ReviewMessageConsumer {

    private final CompanyService companyService;

    @RabbitListener(queues = "companyRatingQueue")
    public void consumeMessage(ReviewMessage reviewMessage) {
        companyService.updateCompanyRating(reviewMessage);
    }


}
